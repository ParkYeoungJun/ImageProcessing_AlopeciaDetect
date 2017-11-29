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

    threshold(matInput, matResult, 0, 255, THRESH_OTSU | THRESH_BINARY);
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

JNIEXPORT void JNICALL Java_com_soongsil_alopeciadetect_views_PictureActivity_CanyEdgeDetect
        (JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult, jint lowThreshold, jint ratio, jint kernel_size) {

    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;

    blur(matInput, matResult, Size(3, 3));

    Canny(matResult, matResult, lowThreshold, lowThreshold*ratio, kernel_size);

}
}