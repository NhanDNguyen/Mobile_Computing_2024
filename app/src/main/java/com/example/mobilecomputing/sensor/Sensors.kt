package com.example.mobilecomputing.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.mobilecomputing.AppViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LightSensor(
    context: Context,
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_LIGHT,
    sensorType = Sensor.TYPE_LIGHT
)

@Composable
fun LightSensorOperation(
    lightSensor: LightSensor,
    viewModel: AppViewModel,
    delay: Long = 0
) {
    val coroutineScope = rememberCoroutineScope()
    if (viewModel.getLightSensor()) {
        runBlocking {
            delay(delay)
            coroutineScope.launch {
                lightSensor.startListening()
                lightSensor.setOnSensorValuesChangedListener {values ->
                    if (!viewModel.getLightSensor()) {
                        lightSensor.stopListening()
                        this.cancel()
                    } else {
                        viewModel.updateLightSensorValue(
                            lux = values[0]
                        )
                    }
                }
            }
        }
    }
}