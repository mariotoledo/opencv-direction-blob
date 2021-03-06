package org.opencv.samples.colorblobdetect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ColorBlobDetector
{
	static int colisionArea = 120;
	
	public void setColorRadius(Scalar radius)
	{
		mColorRadius = radius;
	}
	
	public void setHsvColor(Scalar hsvColor)
	{
	    double minH = (hsvColor.val[0] >= mColorRadius.val[0]) ? hsvColor.val[0]-mColorRadius.val[0] : 0; 
    	    double maxH = (hsvColor.val[0]+mColorRadius.val[0] <= 255) ? hsvColor.val[0]+mColorRadius.val[0] : 255;

  		mLowerBound.val[0] = minH;
   		mUpperBound.val[0] = maxH;

  		mLowerBound.val[1] = hsvColor.val[1] - mColorRadius.val[1];
   		mUpperBound.val[1] = hsvColor.val[1] + mColorRadius.val[1];

  		mLowerBound.val[2] = hsvColor.val[2] - mColorRadius.val[2];
   		mUpperBound.val[2] = hsvColor.val[2] + mColorRadius.val[2];

   		mLowerBound.val[3] = 0;
   		mUpperBound.val[3] = 255;

   		Mat spectrumHsv = new Mat(1, (int)(maxH-minH), CvType.CV_8UC3);

 		for (int j = 0; j < maxH-minH; j++)
   		{
   			byte[] tmp = {(byte)(minH+j), (byte)255, (byte)255};
   			spectrumHsv.put(0, j, tmp);
   		}

   		Imgproc.cvtColor(spectrumHsv, mSpectrum, Imgproc.COLOR_HSV2RGB_FULL, 4);

	}
	
	public Mat getSpectrum()
	{
		return mSpectrum;
	}
	
	public void setMinContourArea(double area)
	{
		mMinContourArea = area;
	}
	
	public int process(Mat rgbaImage)
	{
    	Mat pyrDownMat = new Mat();

    	Imgproc.pyrDown(rgbaImage, pyrDownMat);
    	Imgproc.pyrDown(pyrDownMat, pyrDownMat);

      	Mat hsvMat = new Mat();
    	Imgproc.cvtColor(pyrDownMat, hsvMat, Imgproc.COLOR_RGB2HSV_FULL);

    	Mat Mask = new Mat();
    	Core.inRange(hsvMat, mLowerBound, mUpperBound, Mask);
    	Mat dilatedMask = new Mat();
    	Imgproc.dilate(Mask, dilatedMask, new Mat());

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(dilatedMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find max contour area
        double maxArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        while (each.hasNext())
        {
        	MatOfPoint wrapper = each.next();
        	double area = Imgproc.contourArea(wrapper);
        	if (area > maxArea)
        		maxArea = area;
        }

        // Filter contours by area and resize to fit the original image size
        mContours.clear();
        each = contours.iterator();
        
        int pointsToLeft = 0;
        int pointsToRight = 0;
        int pointsInFront = 0;
        
        //rgbaImage.
        
        while (each.hasNext())
        {
        	MatOfPoint contour = each.next();
        	if (Imgproc.contourArea(contour) > mMinContourArea*maxArea)
        	{
        		for(int i = 0; i < contour.toList().size(); i++) {
        			Point p = contour.toList().get(i);
        			
        			double xMultiplicado = (p.x * rgbaImage.width()) / 78;
        			//System.out.println(">>>>>> " + p.x);
        			//System.out.println("P.X: " + xMultiplicado + " ColisionArea/2: " + (colisionArea / 2) + " RGAImage/2: " + (rgbaImage.width() / 2));
        			if(xMultiplicado > (rgbaImage.width() / 2) - (colisionArea / 2) && xMultiplicado < (rgbaImage.width() / 2) + (colisionArea / 2)){
        				pointsInFront++;
        			} else if (xMultiplicado< (rgbaImage.width() / 2) - (colisionArea / 2)) {
        				pointsToLeft++;
        			} else{
        				pointsToRight++;
        			}
        		}
        		Core.multiply(contour, new Scalar(4,4), contour);
        		mContours.add(contour);
        	}
        }
        
        if(pointsToLeft > pointsToRight && pointsToLeft > pointsInFront){
        	System.out.println(">>>> ESQUERDA | E:" + pointsToLeft + " D:" + pointsToRight + " F:" + pointsInFront);
        	return 0;
        } else if (pointsToRight > pointsToLeft && pointsToRight > pointsInFront){
        	System.out.println(">>>> DIREITA | E:" + pointsToLeft + " D:" + pointsToRight + " F:" + pointsInFront);
        	return 1;
        } else {
        	System.out.println(">>>> FRENTE | E:" + pointsToLeft + " D:" + pointsToRight + " F:" + pointsInFront);
        	return 2;
        }
	}

	public List<MatOfPoint> getContours()
	{
		return mContours;
	}

	// Lower and Upper bounds for range checking in HSV color space
	private Scalar mLowerBound = new Scalar(0);
	private Scalar mUpperBound = new Scalar(0);
	// Minimum contour area in percent for contours filtering
	private static double mMinContourArea = 0.1;
	// Color radius for range checking in HSV color space
	private Scalar mColorRadius = new Scalar(25,50,50,0);
	private Mat mSpectrum = new Mat();
	private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();;
}
