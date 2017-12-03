#include <jni.h>
#include "com_soongsil_alopeciadetect_views_PictureActivity.h"
#include "com_soongsil_alopeciadetect_MainActivity.h"
#include "com_soongsil_alopeciadetect_views_ProcessActivity.h"

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

extern "C" {

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_ConvertRGBtoGray
  (JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult){


        Mat &matInput = *(Mat *)matAddrInput;
        Mat &matResult = *(Mat *)matAddrResult;

        cvtColor(matInput, matResult, CV_RGBA2GRAY);
 }

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_Segmentation
        (JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult) {

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

//    threshold(matInput, matResult, 0, 255, THRESH_OTSU | THRESH_BINARY);
    threshold(matInput, matResult, 140, 255, THRESH_BINARY);

}

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_MorphologyErosion
        (JNIEnv * env, jobject instance, jlong matAddrInput, jlong matAddrResult) {

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

    Mat element5(5, 5, CV_8U, Scalar(1));

    morphologyEx(matInput, matResult, MORPH_ERODE, element5);
}

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_MorphologyOpening
        (JNIEnv * env, jobject instance, jlong matAddrInput, jlong matAddrResult) {

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

    Mat element5(5, 5, CV_8U, Scalar(1));

    morphologyEx(matInput, matResult, MORPH_OPEN, element5);

}

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_MorphologyClosing
        (JNIEnv * env, jobject instance, jlong matAddrInput, jlong matAddrResult) {


    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

    Mat element5(5, 5, CV_8U, Scalar(1));

    morphologyEx(matInput, matResult, MORPH_CLOSE, element5);

}

JNIEXPORT jint JNICALL Java_com_soongsil_alopeciadetect_views_ProcessActivity_IsKeratin
        (JNIEnv * env, jobject instance, jlong matAddrInput, jlong matAddrResult) {

    int rtnScore = 1;

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;
    Mat matGrey, img_labels, stats, centroids, matBinary;

    //gray-scale
    cvtColor(matInput, matGrey, CV_RGB2GRAY);

    //thresholding
    threshold(matGrey, matBinary, 140, 255, THRESH_BINARY);
    threshold(matGrey, matResult, 140, 255, THRESH_BINARY);


    //labeling
    int numOfLabels = connectedComponentsWithStats(~matBinary, img_labels, stats, centroids, 8, CV_32S);
    if(numOfLabels > 1000)	rtnScore = 5;
    else if(numOfLabels > 700)	rtnScore = 4;
    else if(numOfLabels > 500)	rtnScore = 3;
    else if(numOfLabels > 250)	rtnScore = 2;

    return numOfLabels;
}

JNIEXPORT jint JNICALL Java_com_soongsil_alopeciadetect_views_ProcessActivity_IsAlopecia
        (JNIEnv * env, jobject instance, jlong matAddrInput, jlong matAddrResult) {

    int rtnScore = 1;

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;
    Mat matGrey, matBinary;

    //gray-scale
    cvtColor(matInput, matGrey, CV_RGB2GRAY);

    //thresholding
    threshold(matGrey, matBinary, 80, 255, THRESH_BINARY);
    threshold(matGrey, matResult, 80, 255, THRESH_BINARY);

    int sum = 0;

    for(int i = 0 ; i < matResult.rows ; ++i) {
        for(int j = 0 ; j < matResult.cols ; ++j) {
            if (matResult.at<uchar>(i, j) == 0)
                ++sum;
        }
    }

    return sum;
}

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_CanyEdgeDetect
        (JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult, jint lowThreshold, jint ratio, jint kernel_size) {

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

    blur(matInput, matResult, Size(3, 3));

    Canny(matResult, matResult, lowThreshold, lowThreshold*ratio, kernel_size);

}

JNIEXPORT jint JNICALL Java_com_soongsil_alopeciadetect_views_ProcessActivity_AddrToMat
        (JNIEnv * env, jobject instance, jlong matAddrInput, jlong matAddrResult) {
    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

    threshold(matInput, matResult, 140, 255, THRESH_BINARY);
}

}