# OpenCV Direction Blob
> An example of OpenCV Color Blob Direction to set a direction based on a specific color

This project uses the OpenCV Color Blob Detection example to set a direction based on a selected color. The idea is that an unit control be able to keep track of a line or area of a unique color using computer vision. For that, an Android App was made so we could use it's camera and tap on the desired color to keep it's track.

Our logic is pretty simple:
1. We draw a rectangle on the middle of the screen, so we set a range of vision to our unit control;
2. We draw a line vertically that splits the rectangle in two parts
    1. If the area to the left has more pixels of the selected color than the right area, then we should go to the left
    2. If the area to the right has more pixels of the selected color than the left area, then we should go to the right
    3. If the areas are equally colored, then we should keep ahead

[![Android camera with the project running](http://mariotoledo.github.io/opencv-direction-blob/docs/camera.jpg)]

## Usage
This project was initially used as a first attempt to add computer vision for an semi-autonomous robot that could move freely in a indoor environment. An Android device was connected on a NXT robot using bluetooth connection so we could give instructions to the engines of the robot, and then it could follow a line of a specific color on the ground as seen in the video above:

[![Robot following a black line](https://img.youtube.com/vi/cxLMny_5SNs/0.jpg)](https://www.youtube.com/watch?v=cxLMny_5SNs)

We even publish an article on WCV 2015 showing the process and all our results, available [right here](http://www.lbd.dcc.ufmg.br/colecoes/wvc/2015/020.pdf).

## Development

There are several ways to improve this project. First of all, this can't be added freely to other projects that use OpenCV, so it would be great if there is a way to turn this a package or something else. Thus, the way we decide the direction so much simple, so there should be another ways to improve this code by other algorithms.

Feel free to fork this project and help to improve this code.
