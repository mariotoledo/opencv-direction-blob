This project uses the OpenCV example, for Color Blob Detection.
It`s changes are based to track and direction when an object is selected on screen, covered by it`s blob.
When an object is selected, it`s possible to move the camera along left and right, and check it`s distance from an bounding box. When the object is to the left from the center of the camera, a blue box is shown on the top screen of the camera, while a red box is shown when the object is to the right. When the object is intersected by the bounding box, a green box appears on the center.

This code is being used to track a line path on the camera, to move a robot by bluetooth.