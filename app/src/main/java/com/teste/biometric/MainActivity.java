package com.teste.biometric;
import android.app.VoiceInteractor;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.os.HardwarePropertiesManager;
import android.os.SystemClock;
import android.os.UserManager;
import android.provider.Settings;
import android.security.identity.IdentityCredential;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.UUID;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity  {

    private Button authBtn;
    private TextView authStatusTv;
    private Executor executor;
    private BiometricPrompt biometricPrompt;

    private BiometricManager.Authenticators aut;
    private BiometricPrompt.PromptInfo promptInfo;
    private String mToBeSignedMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authBtn = findViewById(R.id.authBtn);
        authStatusTv = findViewById(R.id.authStatusTv);




        // init bio metric
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            private IdentityCredential mCredential;

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // erro na autenticação
                authStatusTv.setText("Erro na leitura: "+ errString);
                Toast.makeText(MainActivity.this, "Erro na leitura: "+ errString, Toast.LENGTH_SHORT).show();
            }

           
            

            //  byte[] signature = mSig.getBytes();
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                authStatusTv.setText("Sucesso na leitura!");
                Toast.makeText(MainActivity.this, "Sucesso na leitura!", Toast.LENGTH_LONG).show();
             if(result.toString() != "Daniel"){
                 System.out.println("**************" + result+"***************");

             }

            }


            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authStatusTv.setText("Falha na leitura, tente novamente");
                Toast.makeText(MainActivity.this, "Falha na leitura, tente novamente", Toast.LENGTH_SHORT).show();
            }
        });

        //setup title description on auth authentication
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Leitura biométrica")
                .setSubtitle("Registre seu ponto com a digital")
                .setConfirmationRequired(true)
               // .setDeviceCredentialAllowed(Bio)
               .setNegativeButtonText("Cancelar")
                .build();


        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         // show auth diaslog
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}