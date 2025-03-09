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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


@TeleOp(name="Code OpMode (Into the deep)", group="Iterative OpMode")
public class TeleOpITD extends OpMode {

    private	double targetPosition;
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx garra, extencao, motor_e, motor_d;
    private Servo ServoMove = null;



    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        //M 3 Braço
        garra	= hardwareMap.get(DcMotorEx.class, "garra");
        //M 0 extençao
        extencao = hardwareMap.get(DcMotorEx.class, "extencao");


        //S 2 Servo garra movimentaçao (cima e baixo la ele)
        ServoMove = hardwareMap.get(Servo.class, "ServoMove");


        //M 2 Motor esquerdo
        motor_e = hardwareMap.get(DcMotorEx.class, "motor_e");
        //M 1 Motor Direito
        motor_d = hardwareMap.get(DcMotorEx.class, "motor_d");

        garra.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); garra.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        targetPosition = 0;

        // Motores
        motor_e.setDirection(DcMotor.Direction.REVERSE); motor_d.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
                        //Braço
        double controle = -gamepad1.left_stick_y;
        double poder;
        if (controle > 0.1 || controle < -0.1) {
            poder = controle;
            targetPosition = garra.getCurrentPosition();
            garra.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else {
            garra.setTargetPosition((int)targetPosition);
            garra.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            poder =- 0.4;
        }
        garra.setPower(poder);

        telemetry.addData("Joystick", controle);
        telemetry.addData("Target Position", targetPosition);
        telemetry.addData("Current Position", garra.getCurrentPosition());

                        //Extençao

        int TargetPositionE = 0;
        double PoderE = gamepad1.right_stick_y;
        extencao.setPower(PoderE);
        telemetry.addData("Extençora Current Position", extencao.getCurrentPosition());

                            //Garra
        if (gamepad1.right_bumper) {
            ServoMove.setPosition(1);
            telemetry.addData("ServoStatusMove", "on(cima)");
        } else if (gamepad1.left_bumper) {
            ServoMove.setPosition(0);
            telemetry.addData("ServoStatusMove", "on(baixo)");
        } else {
            ServoMove.setPosition(0.5);
            telemetry.addData("ServoStatusMove", "off");
        }

                        //Movimentaçao.
        double leftPower;
        double rightPower;
        double drive = gamepad2.left_stick_y;
        double turn  =  gamepad2.right_stick_x;

        double motoreP = motor_e.getCurrent(CurrentUnit.AMPS);
        double motoreD = motor_d.getCurrent(CurrentUnit.AMPS);
        double extencaoP = extencao.getCurrent(CurrentUnit.AMPS);
        double garrap = garra.getCurrent(CurrentUnit.AMPS);

        leftPower = Range.clip((drive + turn) * 10, -1.0, 1.0);
        rightPower = Range.clip((drive - turn) * 10, -1.0, 1.0);

        double divisao = 1;

        if (gamepad2.right_bumper) {
            divisao = 2;
        }


        motor_e.setPower(leftPower/divisao);
        motor_d.setPower(rightPower/divisao);

                        //Telemetria
        telemetry.addLine("Gastos em AMPS | ")
                .addData("Motor Esquerdo", motoreP)
                .addData("Motor Direito", motoreD)
                .addData("Motor Extençora", extencaoP)
                .addData("Motor Braço", garrap);
        telemetry.addLine("Status")
                .addData("Status", "Run Time: " + runtime.toString())
                .addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.update();
    }

    @Override
    public void stop() {

        motor_e.setPower(0);
        motor_d.setPower(0);
    }

}
