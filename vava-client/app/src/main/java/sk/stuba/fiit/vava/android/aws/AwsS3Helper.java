package sk.stuba.fiit.vava.android.aws;

import android.content.Context;
import android.support.annotation.NonNull;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * AWS S3 storage access tools
 */
public class AwsS3Helper {

    /**
     * Get name of bucket where images are stored
     * @return Bucket name
     */
    public static String getBucketName() {
        return AwsS3Constants.AWS_S3_BUCKET;
    }

    /**
     * Build accessible url for S3 stored file
     * @param fileName File name including extension
     * @return Full url for file
     */
    public static String buildS3ImageUrl(@NonNull String fileName) {
        return AwsS3Constants.AWS_S3_URL + "/" + fileName;
    }

    /**
     * Build credentials provider for particular region
     * @param context Context trying to access credentials
     * @return Cognito identity persistence class instance
     */
    private static CognitoCachingCredentialsProvider buildCognitoCachingCredentialsProvider(
            @NonNull Context context) {
        return new CognitoCachingCredentialsProvider(
                context,
                AwsS3Constants.AWS_S3_ACCOUNT_ID,
                Regions.US_WEST_2
        );
    }

    /**
     * Build authenticated client on credentials
     * @param context Context trying to access client
     * @return Cognito authenticated S3 client
     */
    private static AmazonS3Client buildAmazonS3Client(@NonNull Context context) {
        return new AmazonS3Client(buildCognitoCachingCredentialsProvider(context));
    }

    /**
     * Build S3 transfer utility
     * @param context Context trying to access utility
     * @return Utility able to transfer files onto S3
     */
    public static TransferUtility buildTransferUtility(@NonNull Context context) {
        return new TransferUtility(buildAmazonS3Client(context), context);
    }
}
