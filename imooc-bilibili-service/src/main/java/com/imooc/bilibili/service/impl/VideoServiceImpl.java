package com.imooc.bilibili.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.domain.video.*;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.VideoMapper;
import com.imooc.bilibili.service.*;
import com.imooc.bilibili.util.FastDFSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VideoTagService videoTagService;

    @Resource
    private VideoLikeService videoLikeService;

    @Resource
    private VideoCollectionService videoCollectionService;

    @Resource
    private VideoCoinService videoCoinService;

    @Resource
    private UserCoinService userCoinService;

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private FastDFSUtil fastDFSUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVideo(Video video) {
        video.setCreateTime(new Date());
        videoMapper.insert(video);
        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        videoTagList.forEach(item -> {
            item.setCreateTime(new Date());
            item.setVideoId(videoId);
        });
        videoTagService.saveBatch(videoTagList);
    }

    @Override
    public PageResult<Video> pageListVideos(Integer pageNum, Integer pageSize, String area) {
        if (pageNum == null || pageSize == null) {
            throw new ConditionException("分页参数异常");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start", (pageNum - 1) * pageSize);
        params.put("limit", pageSize);
        params.put("area", area);
        List<Video> list = new ArrayList<>();
        Integer total = videoMapper.pageCountVideos(params);
        if (total > 0) {
            list = videoMapper.pageListVideos(params);
        }
        return new PageResult<>(total, list);
    }

    @Override
    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
        } catch (Exception e) {
            log.error("请求处理异常", e);
        }
    }

    @Override
    public void addVideoLike(Long userId, Long videoId) {
        // 获取到当前视频信息
        Video video = videoMapper.selectById(videoId);
        if (ObjectUtil.isNull(video)) {
            throw new ConditionException("没有视频相关信息");
        }
        // 获取点赞表,查看是否已经点过赞
        VideoLike videoLike = videoLikeService.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if (ObjectUtil.isNotNull(video)) {
            throw new ConditionException("该视频已经点赞过");
        }
        videoLike = new VideoLike();
        videoLike.setUserId(userId);
        videoLike.setVideoId(videoId);
        videoLike.setCreateTime(new Date());
        videoLikeService.save(videoLike);
    }

    @Override
    public void deleteVideoLike(Long userId, Long videoId) {
        videoLikeService.deleteVideoLikeByVideoIdAndUserId(videoId, userId);
    }

    @Override
    public Map<String, Object> getVideoLikeCount(Long videoId, Long userId) {
        Long videoLikeCount = videoLikeService.getVideoLikeCountByVideoId(videoId);
        VideoLike videoLike = videoLikeService.getVideoLikeByVideoIdAndUserId(videoId, userId);
        boolean like = ObjectUtil.isNotNull(videoLike);
        Map<String, Object> result = new HashMap<>();
        result.put("count", videoLikeCount);
        result.put("like", like);
        return result;
    }

    @Override
    public void addVideoCollection(VideoCollection videoCollection) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if (ObjectUtil.isNull(videoId) || ObjectUtil.isNull(groupId)) {
            throw new ConditionException("参数异常:videoId||groupId为null");
        }
        Video video = videoMapper.selectById(videoId);
        if (ObjectUtil.isNull(video)) {
            throw new ConditionException("没有找到对应的该视频数据,videoId:" + videoId);
        }

        // 先删除原有的视频收藏,再添加视频收藏
        videoCollectionService.deleteVideoCollection(videoId, videoCollection.getUserId());
        videoCollection.setCreateTime(new Date());
        videoCollectionService.addVideoCollection(videoCollection);
    }

    @Override
    public void deleteVideoCollection(Long videoId, Long userId) {
        videoCollectionService.deleteVideoCollection(videoId, userId);
    }

    @Override
    public Map<String, Object> getVideoCollectionCount(Long videoId, Long userId) {
        Long videoCollectionCount = videoCollectionService.getVideoCollectionCountByVideoId(videoId);
        VideoCollection videoCollection = videoCollectionService.getVideoCollectionByVideoIdAndUserId(videoId, userId);
        boolean collection = ObjectUtil.isNotNull(videoCollection);
        Map<String, Object> result = new HashMap<>();
        result.put("count", videoCollectionCount);
        result.put("collection", collection);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVideoCoin(VideoCoin videoCoin) {
        Long videoId = videoCoin.getVideoId();
        Integer amount = videoCoin.getAmount();
        if (ObjectUtil.isNull(videoId) || ObjectUtil.isNull(amount)) {
            throw new ConditionException("videoId||amount为空");
        }
        Integer userCoinAmount = userCoinService.getUserCoinAmountByUserId(videoCoin.getUserId());
        if (ObjectUtil.isNull(userCoinAmount)) {
            userCoinAmount = 0;
            UserCoin userCoin = new UserCoin();
            userCoin.setUserId(videoCoin.getUserId());
            userCoin.setAmount(userCoinAmount);
            userCoin.setCreateTime(new Date());
            userCoin.setUpdateTime(new Date());
            userCoinService.addUserCoin(userCoin);
        }
        if (amount > userCoinAmount) {
            throw new ConditionException("硬币数量不足");
        }
        // 查询该用户对该视频投了多少个币
        videoCoin.setUpdateTime(new Date());
        VideoCoin dbVideoCoin = videoCoinService.getVideoCoinByVideoIdAndUserId(videoId, videoCoin.getUserId());
        if (ObjectUtil.isNull(dbVideoCoin)) {
            videoCoin.setCreateTime(new Date());
            videoCoinService.addVideoCoin(videoCoin);
        } else {
            videoCoin.setId(dbVideoCoin.getId());
            videoCoin.setAmount(amount + dbVideoCoin.getAmount());
            videoCoinService.updateVideoCoinById(videoCoin);
        }
        // 更新用户硬币的总数
        userCoinService.updateUserCoinAmountByUserId(videoCoin.getUserId(), (userCoinAmount - amount));
    }

    @Override
    public Map<String, Object> getVideoCoinCount(Long videoId, Long userId) {
        Long videoCoinAmount = videoCoinService.getVideoCoinAmountByVideoId(videoId);
        VideoCoin videoCoin = videoCoinService.getVideoCoinByVideoIdAndUserId(videoId, userId);
        boolean coin = ObjectUtil.isNotNull(videoCoin);
        Map<String, Object> result = new HashMap<>();
        result.put("amount", videoCoinAmount);
        result.put("coin", coin);
        return result;
    }

    @Override
    public void addVideoComment(VideoComment videoComment) {
        Long videoId = videoComment.getVideoId();
        if (ObjectUtil.isNull(videoId)) {
            throw new ConditionException("参数异常:videoId为null");
        }
        Video video = videoMapper.selectById(videoId);
        if (ObjectUtil.isNull(video)) {
            throw new ConditionException("没有找到对应的该视频数据,videoId:" + videoId);
        }
        videoComment.setCreateTime(new Date());
        videoComment.setUpdateTime(new Date());
        videoCommentService.addVideoComment(videoComment);
    }

    @Override
    public PageResult<VideoComment> pageListVideoComment(Integer pageSize, Integer pageNum, Long videoId) {
        Video video = videoMapper.selectById(videoId);
        if (ObjectUtil.isNull(video)) {
            throw new ConditionException("没有找到对应的该视频数据,videoId:" + videoId);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start", (pageNum - 1) * pageSize);
        params.put("limit", pageSize);
        params.put("videoId", videoId);
        Integer total = videoCommentService.pageCountVideoComment(params);
        List<VideoComment> list = new ArrayList<>();
        if (total > 0) {
            list = videoCommentService.pageListVideoComment(params);
            // 批量查询二级评论
            // 提取Id列表
            List<Long> parentIdList = list.stream().map(VideoComment::getId).collect(Collectors.toList());
            List<VideoComment> childCommentList = videoCommentService.batchGetVideoCommentByRootId(parentIdList);
            // 批量查询用户信息
            Set<Long> userIdList = list.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            Set<Long> replyUserIdList = childCommentList.stream().map(VideoComment::getReplyUserId).collect(Collectors.toSet());
            userIdList.addAll(replyUserIdList);
            List<UserInfo> userInfoList = userInfoService.batchGetUserInfoByUserIds(userIdList);
            Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
            list.forEach(comment -> {
                Long id = comment.getId();
                List<VideoComment> childList = new ArrayList<>();
                childCommentList.forEach(child -> {
                    if (id.equals(child.getRootId())) {
                        child.setUserInfo(userInfoMap.get(child.getUserId()));
                        child.setReplyUserInfo(userInfoMap.get(child.getReplyUserId()));
                        childList.add(child);
                    }
                });
                comment.setChildList(childList);
                comment.setUserInfo(userInfoMap.get(comment.getUserId()));
            });
        }
        return new PageResult<>(total, list);
    }

    @Override
    public Map<String, Object> getVideoDetail(Long videoId) {
        Video video = videoMapper.selectById(videoId);
        Long userId = video.getUserId();
        UserInfo userInfo = userInfoService.getById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("video", video);
        result.put("userInfo", userInfo);
        return result;
    }
}