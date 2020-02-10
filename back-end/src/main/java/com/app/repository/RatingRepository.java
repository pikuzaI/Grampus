package com.app.repository;

import com.app.DTO.DTOLikableProfile;
import com.app.configtoken.Constants;
import com.app.entities.Rating;
import com.app.enums.Mark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(
            value = "SELECT id, profile_id, rating_source_username,profile_id as user_id, rating_type, COUNT(rating_type) as raiting_count FROM ratings GROUP BY profile_id, rating_type",
            nativeQuery = true)
    List<Rating> findAllRatingById();

    @Query(value = "SELECT COUNT(rating_type) FROM ratings WHERE profile_id = ? AND rating_type = ?",
            nativeQuery = true)
    Long countRatingType(Long id, String ratingType);

    @Query(value = "SELECT * FROM ratings WHERE profile_id = ? AND rating_type is null",
            nativeQuery = true)
    Rating countRatingType(Long id);

    @Query("SELECT NEW com.app.DTO.DTOLikableProfile(r.profileRating.user.id, r.profileRating.user.fullName, r.profileRating.user.jobTitle, r.profileRating.profilePicture) " +
            "FROM  Rating r WHERE r.ratingType = :ratingType")
    Set<DTOLikableProfile> findProfileByRatingType(@Param("ratingType") Mark ratingType);

    @Query (value = "SELECT rating_type FROM ratings WHERE profile_id = ? AND rating_source_username = ?",
            nativeQuery = true)
    String checkLike(Long profileId, String currentUserEmail);

    @Query("SELECT COUNT(r.ratingType) FROM Rating r WHERE r.profileRating.user.id = :id AND r.ratingType NOT LIKE :ratingType")
    Long countProfileLikes(@Param("id")Long id, @Param("ratingType") Mark ratingType);

    @Query("SELECT COUNT(r.ratingType) FROM Rating r WHERE r.profileRating.user.id = :id AND r.ratingType LIKE :ratingType")
    Long countProfileDislikes(@Param("id")Long id, @Param("ratingType") Mark ratingType);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike or r.ratingType IS NULL THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id AND r.profileRating.user.id IN :userIds" +
            " GROUP BY u.id " +
            "ORDER BY likecount DESC")
    Page<DTOLikableProfile> findProfilesSubscriptionsWithoutSearchParamAndWithoutRatingType(@Param("userIds") Set<Long> userIds, @Param("dislike") Mark dislike, Pageable p);


    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike or r.ratingType IS NULL THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end) as dislikecount)" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id AND r.profileRating.user.id IN :userIds" +
            " GROUP BY u.id " +
            "ORDER BY dislikecount DESC")
    Page<DTOLikableProfile> findProfilesSubscriptionsWithoutSearchParamAndByDislikeType(@Param("userIds") Set<Long> userIds, @Param("dislike") Mark dislike, Pageable p);


    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType like :ratingType THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id " +
            " AND r.profileRating.user.id IN :userIds" +
            " GROUP BY u.id " +
            " ORDER BY likecount DESC")
    Page<DTOLikableProfile> findProfilesSubscriptionsWithoutSearchParamAndByRatingType(@Param("userIds") Set<Long> userIds, @Param("ratingType") Mark ratingType, @Param("dislike") Mark dislike, Pageable p);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike THEN 'like' else null end) as likecount," +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " GROUP BY u.id " +
            " ORDER BY likecount DESC")
    Page<DTOLikableProfile> findAllProfilesWithoutSearchParamAndWithoutRatingType(@Param("dislike") Mark dislike, Pageable p);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike THEN 'like' else null end)," +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end) as dislikecount)" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " GROUP BY u.id " +
            " ORDER BY dislikecount DESC")
    Page<DTOLikableProfile> findAllProfilesWithoutSearchParamAndByDislikeType(@Param("dislike") Mark dislike, Pageable p);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType like :ratingType THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " GROUP BY u.id " +
            " ORDER BY likecount DESC")
    Page<DTOLikableProfile> findAllProfilesWithoutSearchParamAndByRatingType(@Param("ratingType") Mark ratingType, @Param("dislike") Mark dislike, Pageable p);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType like :ratingType THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " AND r.profileRating.user.id IN :userIds " +
            "AND u.fullName LIKE %:searchParam%" +
            " GROUP BY u.id " +
            " ORDER BY likecount DESC")
    Page<DTOLikableProfile> findProfilesSubscriptionsBySearchParamAndRatingType(@Param("userIds") Set<Long> userIds, @Param("ratingType") Mark ratingType, @Param("dislike") Mark dislike, @Param("searchParam") String searchParam, Pageable p);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " AND r.profileRating.user.id IN :userIds" +
            " AND u.fullName LIKE %:searchParam%" +
            " GROUP BY u.id " +
            " ORDER BY likecount DESC")
    Page<DTOLikableProfile> findProfilesSubscriptionsBySearchParamWithoutRatingType(@Param("userIds") Set<Long> userIds, @Param("dislike") Mark dislike, @Param("searchParam") String searchParam, Pageable p);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike THEN 'like' else null end), " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end) as dislikecount)" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " AND r.profileRating.user.id IN :userIds" +
            " AND u.fullName LIKE %:searchParam%" +
            " GROUP BY u.id " +
            " ORDER BY dislikecount DESC")
    Page<DTOLikableProfile> findProfilesSubscriptionsBySearchParamAndByDislikeType(@Param("userIds") Set<Long> userIds, @Param("dislike") Mark dislike, @Param("searchParam") String searchParam, Pageable p);


    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike or r.ratingType IS NULL THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " AND u.fullName LIKE %:searchParam%" +
            " GROUP BY u.id " +
            " ORDER BY likecount DESC")
    Page<DTOLikableProfile> findAllProfilesBySearchParamWithoutRatingType(@Param("dislike") Mark dislike, @Param("searchParam") String searchParam, Pageable p);

    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType not like :dislike or r.ratingType IS NULL THEN 'like' else null end), " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end) as dislikecount)" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " AND u.fullName LIKE %:searchParam%" +
            " GROUP BY u.id " +
            " ORDER BY dislikecount DESC")
    Page<DTOLikableProfile> findAllProfilesBySearchParamAndByDislikeType(@Param("dislike") Mark dislike, @Param("searchParam") String searchParam, Pageable p);


    @Query("SELECT DISTINCT NEW com.app.DTO.DTOLikableProfile (u.id, u.fullName, u.jobTitle, p.profilePicture," +
            "count(case when r.ratingType like :ratingType THEN 'like' else null end) as likecount, " +
            "count(case when r.ratingType like :dislike THEN 'dislike' else null end))" +
            " FROM Rating r JOIN Profile p ON p.id = r.profileRating.user.id JOIN User u ON u.id = p.id" +
            " AND u.fullName LIKE %:searchParam%" +
            " GROUP BY u.id " +
            " ORDER BY likecount DESC")
    Page<DTOLikableProfile> findAllProfilesBySearchParamAndRatingType(@Param("ratingType") Mark ratingType, @Param("dislike") Mark dislike, @Param("searchParam") String searchParam, Pageable p);

    @Query(value = "SELECT * FROM ratings WHERE comment is not NULL and profile_id = ?",
            nativeQuery = true)
    List<Rating> findAllCommentByProfileId(Long id);
}