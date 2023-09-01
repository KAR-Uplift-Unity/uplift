package com.KARUpliftUnity.schedulingtasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.KARUpliftUnity.models.*;
import com.KARUpliftUnity.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final PostRepository postDao;

    private final UserRepository userDao;

    private final UpdateRepository updateDao;

    private final TagRepository tagDao;

    private final LikeRepository likeDao;

    private final ImageRepository imgDao;

    private final CommentRepository comDao;

    private final CategoryRepository catDao;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ScheduledTasks(PostRepository postDao, UserRepository userDao, UpdateRepository updateDao, TagRepository tagDao, LikeRepository likeDao, ImageRepository imgDao, CommentRepository comDao, CategoryRepository catDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.updateDao = updateDao;
        this.tagDao = tagDao;
        this.likeDao = likeDao;
        this.imgDao = imgDao;
        this.comDao = comDao;
        this.catDao = catDao;
    }
// every minute will tell the time
    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }


// every hour will test if there are archived users or posts to remove from the db
    @Scheduled(fixedRate = 3600000)
    public void deleteArchivedItems() {
        List<Post> posts = postDao.findByArchiveTrue();
        List<User> users = userDao.findAllByArchiveTrue();

        Date curTime = new Date();

        long curTimeLong = curTime.getTime();

        for (Post post: posts){
            long archiveTime = 0;
            if (post.getArchive()){
                archiveTime = post.getArchiveDate().getTime();
                System.out.println("archiveTime = " + archiveTime);
            }
            long totalTime = curTimeLong - archiveTime;
            long sevenDays = 604800000;
            if(totalTime > sevenDays) {

                List<Image> images = imgDao.findAllByPost(post);
                if (images != null){
                    imgDao.deleteAllInBatch(images);
                }

                List<Update> updates = updateDao.findAllByPost(post);
                if (updates != null){
                    updateDao.deleteAllInBatch(updates);
                }

                List<Like> likes = likeDao.findAllByPost(post);
                if (likes != null){
                    likeDao.deleteAllInBatch(likes);
                }

                List<Comment> comments = comDao.findAllByPost(post);
                if (comments != null){
                    comDao.deleteAllInBatch(comments);
                }

                List<Tag> tags = tagDao.findAllByPost(post);
                if (tags != null){
                    tagDao.deleteAllInBatch(tags);
                }
                postDao.delete(post);
            }
        }


        for (User user: users){
            long archiveTime = 0;
            if (user.getArchive()){
                archiveTime = user.getArchiveDate().getTime();
            }
            long totalTime = curTimeLong - archiveTime;
            long thirtyDays = 2592000000L;
            if(totalTime > thirtyDays) {
                List<Like> likes = likeDao.findAllByUser(user);
                if (likes != null){
                    likeDao.deleteAllInBatch(likes);
                }
                List<Comment> comments = comDao.findAllByUser(user);
                if (comments != null){
                    comDao.deleteAllInBatch(comments);
                }
                List<Post> usersPosts = postDao.findAllByUser(user);
                if (usersPosts != null){
                    for (Post post : usersPosts){
                        List<Image> images = imgDao.findAllByPost(post);
                        if (images != null){
                            imgDao.deleteAllInBatch(images);
                        }

                        List<Update> updates = updateDao.findAllByPost(post);
                        if (updates != null){
                            updateDao.deleteAllInBatch(updates);
                        }

                        List<Like> likes1 = likeDao.findAllByPost(post);
                        if (likes1 != null){
                            likeDao.deleteAllInBatch(likes1);
                        }

                        List<Comment> comments1 = comDao.findAllByPost(post);
                        if (comments1 != null){
                            comDao.deleteAllInBatch(comments1);
                        }
                        List<Tag> tags = tagDao.findAllByPost(post);
                        if (tags != null){
                            tagDao.deleteAllInBatch(tags);
                        }
                    }
                    postDao.deleteAllInBatch(usersPosts);
                }
                userDao.delete(user);
            }
        }
    }
}
