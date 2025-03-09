package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Code Autonomous (Into the deep)", group="Autonomous")
public class AutonomoITD extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx motor_e, motor_d, garra, extencao;
    private Servo servoMove;

    @Override
    public void runOpMode() {

        motor_e = hardwareMap.get(DcMotorEx.class, "motor_e"); motor_d = hardwareMap.get(DcMotorEx.class, "motor_d");
        garra = hardwareMap.get(DcMotorEx.class, "garra"); extencao = hardwareMap.get(DcMotorEx.class, "extencao");
        servoMove = hardwareMap.get(Servo.class, "ServoMove");

        garra.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER); garra.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        extencao.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER); extencao.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        garra.setTargetPosition(480); extencao.setTargetPosition(-2000);

        waitForStart();
        runtime.reset();

        boolean ativado = true;

        while (opModeIsActive()) {
            if (ativado) {
                Ato1();
                ativado = false;
            }
        }
    }

    private void Ato1() {


                    //CLIPAGEM
        garra.setMode(DcMotorEx.RunMode.RUN_TO_POSITION); garra.setPower(1);
        servoMove.setPosition(1);
        sleep(1000);

        extencao.setMode(DcMotorEx.RunMode.RUN_TO_POSITION); extencao.setPower(1);
        W();
        sleep(2500);

        Stop();
        sleep(2000);

        extencao.setTargetPosition(0);
        S();
        sleep(1000);

        servoMove.setPosition(-1);
        sleep(1000);

        garra.setTargetPosition(0);
        servoMove.setPosition(0.5);
        S();
        sleep(200);

        stop();
        sleep(3000);

                    //ESTACIONAR
        garra.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODERS);
        extencao.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODERS);
        garra.setPower(0);
        extencao.setPower(0);
        D();
        sleep(500);

        W();
        sleep(1000);

        A();
        sleep(500);

        W();
        sleep(3000);

        motor_e.setPower(0);
        motor_d.setPower(0);
    }

    private void D() {
        motor_e.setPower(0);
        motor_d.setPower(1);
    }

    private void A() {
        motor_e.setPower(0);
        motor_d.setPower(1);
    }

    private void W() {
        int divisao = 1;
        motor_e.setPower(-1/2);
        motor_d.setPower(1/2);
    }

    private void S() {
        int divisao = 1;
        motor_e.setPower(1/2);
        motor_d.setPower(-1/2);
    }

    private void Stop() {
        motor_e.setPower(0);
        motor_d.setPower(0);
    }
}

