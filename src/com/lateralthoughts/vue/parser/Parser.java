package com.lateralthoughts.vue.parser;

import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.aisles.datamodels.AisleContext;
import com.aisles.datamodels.AisleImageDetails;
import com.aisles.datamodels.AisleWindowContent;
import com.aisles.datamodels.ImageComment;
import com.aisles.datamodels.ImageRating;
import com.aisles.datamodels.UrlConstants;
import com.card.utils.AppConstants;

public class Parser {
    // ========================= START OF PARSING TAGS
    // ========================================================
    // the following strings are pre-defined to help with JSON parsing
    // the tags defined here should be in sync the API documentation for the
    // backend
    // AisleImageObjects but re-uses the
    // imageItemsArray. Instead the called function clones and keeps a copy.
    // This is pretty inconsistent.
    // Let the allocation happen in one place for both items. Fix this!
    private boolean isEmptyAilseCached = false;
    
    public ArrayList<AisleWindowContent> parseTrendingAislesResultData(
            String resultString, boolean loadMore) {
        
        ArrayList<AisleWindowContent> aisleWindowContentList = new ArrayList<AisleWindowContent>();
        
        JSONArray contentArray = null;
        contentArray = handleResponse(resultString, loadMore);
        if (contentArray == null) {
            return aisleWindowContentList;
        }
        try {
            isEmptyAilseCached = true;
            aisleWindowContentList = parseAisleInformation(contentArray,
                    isEmptyAilseCached);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aisleWindowContentList;
    }
    
    private JSONArray handleResponse(String resultString, boolean loadMore) {
        JSONArray contentArray = null;
        try {
            contentArray = new JSONArray(resultString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentArray;
    }
    
    public AisleImageDetails parseAisleImageData(JSONObject jsonObject)
            throws JSONException {
        // get the image data from image object
        AisleImageDetails aisleImageDetails = new AisleImageDetails();
        aisleImageDetails.mId = jsonObject
                .getString(AppConstants.AISLE_IMAGE_ID);
        aisleImageDetails.mTitle = jsonObject
                .getString(AppConstants.AISLE_IMAGE_TITLE);
        aisleImageDetails.mAvailableHeight = jsonObject
                .getInt(AppConstants.AISLE_IMAGE_HEIGHT);
        aisleImageDetails.mStore = jsonObject
                .getString(AppConstants.AISLE_IMAGE_STORE);
        aisleImageDetails.mImageUrl = jsonObject
                .getString(AppConstants.AISLE_IMAGE_IMAGE_URL);
        Log.i("ImageUrl", "ImageUrl: " + aisleImageDetails.mImageUrl);
        aisleImageDetails.mAvailableWidth = jsonObject
                .getInt(AppConstants.AISLE_IMAGE_WIDTH);
        aisleImageDetails.mDetailsUrl = jsonObject
                .getString(AppConstants.AISLE_IMAGE_DETAILS_URL);
        aisleImageDetails.mOwnerUserId = jsonObject
                .getString(AppConstants.AISLE_IMAGE_OWNERUSER_ID);
        aisleImageDetails.mOwnerAisleId = jsonObject
                .getString(AppConstants.AISLE_IMAGE_OWNER_AISLE_ID);
        // get the image rating list
        JSONArray ratingJsonArray = jsonObject
                .getJSONArray(AppConstants.AISLE_IMAGE_RATINGS);
        ArrayList<ImageRating> ratingList = new ArrayList<ImageRating>();
        int ratingLikeCount = 0;
        if (ratingJsonArray != null) {
            ImageRating imgRatings;
            for (int i = 0; i < ratingJsonArray.length(); i++) {
                JSONObject ratingObj = ratingJsonArray.getJSONObject(i);
                imgRatings = new ImageRating();
                imgRatings.mId = ratingObj
                        .getLong(AppConstants.AISLE_IMAGE_RATING_ID);
                imgRatings.mLastModifiedTimestamp = ratingObj
                        .getLong(AppConstants.AISLE_IMAGE_RATING_LASTMODIFIED_TIME);
                imgRatings.mImageRatingOwnerFirstName = ratingObj
                        .getString(AppConstants.AISLE_IMAGE_RATING_OWNER_FIRST_NAME);
                imgRatings.mImageId = ratingObj
                        .getLong(AppConstants.AISLE_IMAGE_RATING_IMAGEID);
                imgRatings.mImageRatingOwnerLastName = ratingObj
                        .getString(AppConstants.AISLE_IMAGE_RATING_OWNER_LAST_NAME);
                imgRatings.mAisleId = ratingObj
                        .getLong(AppConstants.AISLE_IMAGE_RATING_AISLEID);
                imgRatings.mLiked = ratingObj
                        .getBoolean(AppConstants.AISLE_IMAGE_RATING_LIKED);
                if (imgRatings.mLiked) {
                    ratingLikeCount++;
                }
                ratingList.add(imgRatings);
            }
        }
        aisleImageDetails.mRatingsList = ratingList;
        aisleImageDetails.mLikesCount = ratingLikeCount;
        
        // get the image comment data.
        JSONArray jsonArray = jsonObject
                .getJSONArray(AppConstants.AISLE_IMAGE_COMMENTS);
        ArrayList<ImageComments> commentList = new ArrayList<ImageComments>();
        if (jsonArray != null) {
            ImageComments imgComments;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject commnetObj = jsonArray.getJSONObject(i);
                imgComments = new ImageComments();
                imgComments.mId = commnetObj
                        .getLong(AppConstants.AISLE_IMAGE_COMMENTS_ID);
                if (commnetObj.getString(
                        AppConstants.AISLE_IMAGE_COMMENTS_LASTMODIFIED_TIME)
                        .equals("null")) {
                    imgComments.mLastModifiedTimestamp = commnetObj
                            .getLong(AppConstants.AISLE_IMAGE_COMMENTS_CREATED_TIME);
                } else {
                    imgComments.mLastModifiedTimestamp = commnetObj
                            .getLong(AppConstants.AISLE_IMAGE_COMMENTS_LASTMODIFIED_TIME);
                }
                imgComments.mCommenterLastName = commnetObj
                        .getString(AppConstants.AISLE_IMAGE_COMMENTER_LAST_NAME);
                imgComments.mCommenterUrl = commnetObj
                        .getString(AppConstants.IMAGE_COMMENT_OWNER_IMAGE_URL);
                imgComments.mImageId = commnetObj
                        .getLong(AppConstants.AISLE_IMAGE_COMMENTS_IMAGEID);
                
                imgComments.mCommenterFirstName = commnetObj
                        .getString(AppConstants.AISLE_IMAGE_COMMENTER_FIRST_NAME);
                imgComments.mOwnerUserId = commnetObj
                        .getString(AppConstants.AISLE_IMAGE_COMMENTS_USERID);
                
                imgComments.mComment = commnetObj
                        .getString(AppConstants.COMMENT);
                commentList.add(imgComments);
            }
        }
        aisleImageDetails.mCommentsList = commentList;
        return aisleImageDetails;
    }
    
    public ArrayList<AisleWindowContent> getUserAilseLIst(String jsonArray) {
        ArrayList<AisleWindowContent> aisleWindowContentList = new ArrayList<AisleWindowContent>();
        try {
            
            JSONObject jsonResponse = new JSONObject(new String(jsonArray));
            JSONArray aisleArray = jsonResponse.getJSONArray("aisles");
            
            if (aisleArray != null) {
                isEmptyAilseCached = true;
                aisleWindowContentList = parseAisleInformation(aisleArray,
                        isEmptyAilseCached);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aisleWindowContentList;
    }
    
    public ArrayList<AisleImageDetails> getImagesForAisleId(String aisleId)
            throws Exception {
        ArrayList<AisleImageDetails> imageList = new ArrayList<AisleImageDetails>();
        String imageRequestUrl = UrlConstants.GET_IMAGELIST_RESTURL + aisleId;
        URL url = new URL(imageRequestUrl);
        HttpGet httpGet = new HttpGet(url.toString());
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getEntity() != null
                && response.getStatusLine().getStatusCode() == 200) {
            String responseMessage = EntityUtils.toString(response.getEntity());
            if (responseMessage != null) {
                try {
                    JSONObject mainJsonObject = new JSONObject(responseMessage);
                    JSONArray jsonArray = mainJsonObject.getJSONArray("images");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        AisleImageDetails aisleImageDetails = parseAisleImageData(jsonObject);
                        if (aisleImageDetails.mImageUrl != null
                                && (!aisleImageDetails.mImageUrl
                                        .contains("randomurl.com"))
                                && aisleImageDetails.mImageUrl.trim().length() > 0
                                && aisleImageDetails.mAvailableHeight != 0
                                && aisleImageDetails.mAvailableWidth != 0) {
                            imageList.add(aisleImageDetails);
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }
        return imageList;
    }
    
    private ArrayList<AisleWindowContent> parseAisleInformation(
            JSONArray jsonArray, boolean isEmptyAilseCached)
            throws JSONException {
        ArrayList<AisleWindowContent> aisleWindowContentList = new ArrayList<AisleWindowContent>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject ailseItem = jsonArray.getJSONObject(i);
            AisleContext aisleContext = parseAisleData(ailseItem);
            ArrayList<AisleImageDetails> aisleImageDetailsList = new ArrayList<AisleImageDetails>();
            try {
                JSONObject imageObject = ailseItem
                        .getJSONObject(AppConstants.AISLE_IMAGE_OBJECT);
                JSONArray ImageListJson = imageObject
                        .getJSONArray(AppConstants.AISLE_IMAGE_LIST);
                for (int index = 0; index < ImageListJson.length(); index++) {
                    AisleImageDetails aisleImageDetails = parseAisleImageData(ImageListJson
                            .getJSONObject(index));
                    if (aisleImageDetails.mImageUrl != null
                            && (!aisleImageDetails.mImageUrl
                                    .contains("randomurl.com"))
                            && aisleImageDetails.mImageUrl.trim().length() > 0
                            && aisleImageDetails.mAvailableHeight != 0
                            && aisleImageDetails.mAvailableWidth != 0) {
                        aisleImageDetailsList.add(aisleImageDetails);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (aisleImageDetailsList != null
                    && aisleImageDetailsList.size() > 0) {
                
                AisleWindowContent aisleWindowContent = new AisleWindowContent(
                        aisleContext.mAisleId);
                
                aisleWindowContent.addAisleContent(aisleContext,
                        aisleImageDetailsList);
                aisleWindowContentList.add(aisleWindowContent);
            }
        }
        return aisleWindowContentList;
    }
    
    public AisleContext parseAisleData(JSONObject josnObject) {
        AisleContext aisleContext = new AisleContext();
        try {
            aisleContext.mAisleId = josnObject.getString(AppConstants.AISLE_ID);
            String aisleOwnerImageUrl = josnObject
                    .optString(AppConstants.AISLE_OWNER_IMAGE_URL);
            if (aisleOwnerImageUrl == null || aisleOwnerImageUrl.equals("null")) {
                aisleContext.mAisleOwnerImageURL = null;
            } else {
                aisleContext.mAisleOwnerImageURL = aisleOwnerImageUrl;
            }
            String lastName = josnObject
                    .getString(AppConstants.AISLE_OWNER_LASTNAME);
            aisleContext.mCategory = josnObject
                    .getString(AppConstants.AISLE_CATEGORY);
            aisleContext.mBookmarkCount = josnObject
                    .getInt(AppConstants.AISLE_BOOKMARK_COUNT);
            String description = josnObject
                    .getString(AppConstants.AISLE_DESCRIPTION);
            if (description == null || description.equals("null")) {
                aisleContext.mDescription = "";
            } else {
                aisleContext.mDescription = description;
            }
            String occasion = josnObject
                    .getString(AppConstants.AISLE_OCCASSION);
            if (occasion == null || occasion.equals("null")) {
                aisleContext.mOccasion = "";
            } else {
                aisleContext.mOccasion = occasion;
            }
            aisleContext.mName = josnObject.getString(AppConstants.AISLE_NAME);
            String firstName = josnObject
                    .getString(AppConstants.AISLE_OWNER_FIRSTNAME);
            aisleContext.mUserId = josnObject
                    .getString(AppConstants.AISLE_OWNER_USER_ID);
            aisleContext.mLookingForItem = josnObject
                    .getString(AppConstants.AISLE_LOOKINGFOR);
            
            if (firstName == null || firstName.equals("null")) {
                aisleContext.mFirstName = " ";
                firstName = null;
            } else {
                aisleContext.mFirstName = firstName;
            }
            if (lastName == null || lastName.equals("null")) {
                aisleContext.mLastName = " ";
                lastName = null;
            } else {
                aisleContext.mLastName = lastName;
            }
            if (firstName == null && lastName == null) {
                aisleContext.mFirstName = "Anonymous";
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aisleContext;
    }
    
    public static ArrayList<ImageRating> parseRatedImages(String response) {
        ArrayList<ImageRating> imgRatingList = new ArrayList<ImageRating>();
        ImageRating imgRating = null;
        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    imgRating = new ImageRating();
                    imgRating.mId = jsonArray.getJSONObject(i).getLong(
                            AppConstants.AISLE_IMAGE_RATING_ID);
                    imgRating.mImageId = jsonArray.getJSONObject(i).getLong(
                            AppConstants.AISLE_IMAGE_RATING_IMAGEID);
                    imgRating.mAisleId = jsonArray.getJSONObject(i).getLong(
                            AppConstants.AISLE_IMAGE_RATING_AISLEID);
                    imgRating.mLastModifiedTimestamp = jsonArray.getJSONObject(
                            i).getLong(
                            AppConstants.AISLE_IMAGE_RATING_LASTMODIFIED_TIME);
                    imgRating.mImageRatingOwnerFirstName = jsonArray
                            .getJSONObject(i)
                            .getString(
                                    AppConstants.AISLE_IMAGE_RATING_OWNER_FIRST_NAME);
                    imgRating.mImageRatingOwnerLastName = jsonArray
                            .getJSONObject(i)
                            .getString(
                                    AppConstants.AISLE_IMAGE_RATING_OWNER_LAST_NAME);
                    imgRating.mLiked = jsonArray.getJSONObject(i).getBoolean(
                            AppConstants.AISLE_IMAGE_RATING_LIKED);
                    imgRatingList.add(imgRating);
                }
                imgRatingList = removeDuplicateImageRating(imgRatingList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return imgRatingList;
    }
    
    private static ArrayList<ImageRating> removeDuplicateImageRating(
            ArrayList<ImageRating> imgRatingList) {
        ArrayList<ImageRating> dummyList = new ArrayList<ImageRating>();
        ArrayList<ImageRating> tempList = new ArrayList<ImageRating>();
        ArrayList<ImageRating> finalList = new ArrayList<ImageRating>();
        
        for (ImageRating item : imgRatingList) {
            dummyList.add(item);
        }
        for (ImageRating item : imgRatingList) {
            tempList.clear();
            for (ImageRating item2 : dummyList) {
                if (item.mImageId.longValue() == item2.mImageId.longValue()) {
                    tempList.add(item2);
                    
                }
            }
            if (tempList.size() > 0) {
                for (ImageRating tempItem : tempList) {
                    dummyList.remove(tempItem);
                }
                ImageRating finalItem = tempList.get(0);
                for (ImageRating tempItem : tempList) {
                    if (finalItem.mLastModifiedTimestamp < tempItem.mLastModifiedTimestamp) {
                        finalItem = tempItem;
                    }
                }
                finalList.add(finalItem);
            }
        }
        return finalList;
    }
    
    public ImageComment parseCommentResponse(String response) {
        try {
            if (response != null) {
                JSONObject commnetObj = new JSONObject(response);
                ImageComment imgComments = new ImageComment();
                imgComments.setId(commnetObj
                        .getLong(AppConstants.AISLE_IMAGE_COMMENTS_ID));
                if (commnetObj.getString(
                        AppConstants.AISLE_IMAGE_COMMENTS_LASTMODIFIED_TIME)
                        .equals("null")) {
                    imgComments
                            .setCreatedTimestamp(commnetObj
                                    .getLong(AppConstants.AISLE_IMAGE_COMMENTS_CREATED_TIME));
                } else {
                    imgComments
                            .setLastModifiedTimestamp(commnetObj
                                    .getLong(AppConstants.AISLE_IMAGE_COMMENTS_LASTMODIFIED_TIME));
                }
                imgComments
                        .setCommenterLastName(commnetObj
                                .getString(AppConstants.AISLE_IMAGE_COMMENTER_LAST_NAME));
                imgComments.setImageCommentOwnerImageURL(commnetObj
                        .getString(AppConstants.IMAGE_COMMENT_OWNER_IMAGE_URL));
                imgComments.setOwnerImageId(commnetObj
                        .getLong(AppConstants.AISLE_IMAGE_COMMENTS_IMAGEID));
                imgComments
                        .setCommenterFirstName(commnetObj
                                .getString(AppConstants.AISLE_IMAGE_COMMENTER_FIRST_NAME));
                imgComments.setOwnerUserId(Long.valueOf(commnetObj
                        .getString(AppConstants.AISLE_IMAGE_COMMENTS_USERID)));
                imgComments.setComment(commnetObj
                        .getString(AppConstants.COMMENT));
                return imgComments;
            }
            return null;
        } catch (Exception e) {
        }
        return null;
    }
}
