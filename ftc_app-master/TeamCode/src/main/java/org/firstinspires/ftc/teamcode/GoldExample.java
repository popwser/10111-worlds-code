/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldDetector;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.opencv.core.Point;

import org.opencv.core.Rect;

@Disabled
@TeleOp(name="Gold Example", group="TeleOp")
public class GoldExample extends OpMode
{
    // Detector object
    private GoldDetector detector;

    NewHardware hw = new NewHardware();
    @Override
    public void init() {
        telemetry.addData("Status", "DogeCV 2019.1 - Gold Example");
        // Set up detector

        hw.Initialize(hardwareMap, telemetry);

        detector = new GoldDetector(); // Create detector

        detector.VUFORIA_KEY = "AY7vQPD/////AAABmU9/oz/cvkKgnaLVeyvutUdhmuRrj9E05zzYDKGYqvUtbcKkjYxb5pHABw/Cz5LnRM/fp4entLyQHJiuBqFPWzDtDZc2pcTjjZae8A/5wQ2iVUpYX0Nr2FohfoYclHYeIXgl9II7wV57bHn3shcGCOX9PzxsScrmZj8yQnCdErcuU3DWQQgR4qSGKafvq/Jeux60EMqDR98VC+DnbyfAxAso83fgfnE5Kq/LaWSGcuwrpMhPE89m3pWbMnzBXtev6j5R0TZK+5RoHMnLlYFbrR6B/jbtgr2/fk/xs1lIeXRsGJ0GNjZetaZsG/k6P3RwizvQ52XZamQqmsmg8qCjyBHMhO2GU/1Mc0kr/kIexjSZ";

        //detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), DogeCV.CameraMode.BACK, false, hw.sampleCamera); // Initialize it with the app context and camera
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), DogeCV.CameraMode.BACK, false);
        detector.useDefaults();

        // Optional tuning
        detector.downscale = 1; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005; //

        detector.ratioScorer.weight = 5; //
        detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

        detector.enable(); // Start the detector!
    }

    /*
     * Code to run REPEATEDLY when the driver hits INIT
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        Point goldPosPixels = detector.getScreenPosition();
        telemetry.addData("Position", goldPosPixels.x);

        String goldPos = "RIGHT";

        if(goldPosPixels.x < detector.downscaleResolution.width/3)
        {
            goldPos = "LEFT";
        } else if(goldPosPixels.x < (detector.downscaleResolution.width/3)*2) {
            goldPos = "MIDDLE";
        }

        telemetry.addData("Gold Position", goldPos);
        telemetry.update();

        detector.disable();
    }

    /*
     * Code to run REPEATEDLY when the driver hits PLAY
     */
    @Override
    public void loop() {
        telemetry.addData("IsFound: ", detector.isFound());
        Rect rect = detector.getFoundRect();
        if(detector.isFound()) telemetry.addData("Location: ", Integer.toString((int) (rect.x + rect.width*0.5)) + ", " + Integer.toString((int) (rect.y+0.5*rect.height)));
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        // Disable the detector
        if(detector != null) detector.disable();
    }

}
