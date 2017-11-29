#include <jni.h>
#include "com_soongsil_alopeciadetect_MainActivity.h"
#include "com_soongsil_alopeciadetect_views_PictureActivity.h"

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

JNIEXPORT int JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_IsKeratin
        (JNIEnv * env, jobject instance, jlong matAddrInput) {

    int score = 1;

    Mat &matInput = *(Mat *)matAddrInput;

    Mat matGrey, img_labels, stats, centroids, matBinary, result;

    //gray-scale
    cvtColor(matInput, matGrey, CV_RGB2GRAY);

    //thresholding
//    threshold(matGrey, matBinary, 0, 255, THRESH_OTSU | THRESH_BINARY);
    threshold(matGrey, matBinary, 140, 255, THRESH_BINARY);


    //labeling
    int numOfLables = connectedComponentsWithStats(~matBinary, img_labels, stats, centroids, 8, CV_32S);
    if(numOfLables > 1000)	score = 5;
    else if(numOfLables > 700)	score = 4;
    else if(numOfLables > 500)	score = 3;
    else if(numOfLables > 250)	score = 2;

    return score;
}

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_CanyEdgeDetect
        (JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult, jint lowThreshold, jint ratio, jint kernel_size) {

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

    blur(matInput, matResult, Size(3, 3));

    Canny(matResult, matResult, lowThreshold, lowThreshold*ratio, kernel_size);

}
}