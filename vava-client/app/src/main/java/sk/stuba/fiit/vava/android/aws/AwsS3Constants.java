package sk.stuba.fiit.vava.android.aws;

import sk.stuba.fiit.vava.android.BuildConfig;

/**
 * AWS S3 specific constants
 */
class AwsS3Constants {

    // Amazon account ID
    static final String AWS_S3_ACCOUNT_ID = BuildConfig.AWS_S3_ACCOUNT_ID;

    // S3 bucket name
    static final String AWS_S3_BUCKET = "fiitvava";

    // S3 base url
    private static final String AWS_S3_URL_BASE = "https://s3-us-west-2.amazonaws.com";
    static final String AWS_S3_URL = AWS_S3_URL_BASE + "/" + AWS_S3_BUCKET;
}
