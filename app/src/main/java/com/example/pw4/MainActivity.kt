package com.example.electricalcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.pw4.R
import com.example.pw4.ui.theme.PW4Theme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PW4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    calculationOne()
//                    calculationTwo()
//                    calculationThree()
                }
            }
        }
    }


    fun calculationOne() {
        setContentView(R.layout.layout1)
        val resultDisplay = findViewById<TextView>(R.id.result_display)

        val voltageInput = findViewById<EditText>(R.id.input_voltage)
        val currentInput = findViewById<EditText>(R.id.input_current)
        val durationInput = findViewById<EditText>(R.id.input_duration)
        val powerStationInput = findViewById<EditText>(R.id.input_power_station)
        val transformerInput = findViewById<EditText>(R.id.input_transformer)
        val capacityInput = findViewById<EditText>(R.id.input_capacity)
        val calculateButton = findViewById<Button>(R.id.button_calculate)

        calculateButton.setOnClickListener {
            val voltage = voltageInput.text.toString().toDoubleOrNull() ?: 0.0
            val current = currentInput.text.toString().toDoubleOrNull() ?: 0.0
            val duration = durationInput.text.toString().toDoubleOrNull() ?: 0.0
            val powerStation = powerStationInput.text.toString().toDoubleOrNull() ?: 0.0
            val transformer = transformerInput.text.toString().toDoubleOrNull() ?: 0.0
            val capacity = capacityInput.text.toString().toDoubleOrNull() ?: 0.0

            val efficiencyFactor = 1.4
            val thermalConstant = 92.0

            val motorCurrent = (transformer / 2) / (sqrt(3.0) * voltage)
            val motorPeakCurrent = 2 * motorCurrent
            val crossSection = motorCurrent / efficiencyFactor
            val minimumSection = (current * sqrt(duration)) / thermalConstant

            resultDisplay.text = "$motorPeakCurrent, $crossSection, $minimumSection"
        }
    }

    fun calculationTwo() {
        setContentView(R.layout.layout2)
        val resultDisplay = findViewById<TextView>(R.id.result_display)

        val voltageLevelInput = findViewById<EditText>(R.id.input_voltage_level)
        val apparentPowerInput = findViewById<EditText>(R.id.input_apparent_power)
        val shortCircuitVoltageInput = findViewById<EditText>(R.id.input_short_circuit_voltage)
        val ratedPowerInput = findViewById<EditText>(R.id.input_rated_power)
        val calculateButton = findViewById<Button>(R.id.button_calculate)

        calculateButton.setOnClickListener {
            val voltageLevel = voltageLevelInput.text.toString().toDoubleOrNull() ?: 0.0
            val apparentPower = apparentPowerInput.text.toString().toDoubleOrNull() ?: 0.0
            val shortCircuitVoltage = shortCircuitVoltageInput.text.toString().toDoubleOrNull() ?: 0.0
            val ratedPower = ratedPowerInput.text.toString().toDoubleOrNull() ?: 0.0

            val reactiveImpedance = voltageLevel * voltageLevel / apparentPower
            val transformerImpedance = (shortCircuitVoltage / 100) * (voltageLevel * voltageLevel / ratedPower)
            val totalImpedance = reactiveImpedance + transformerImpedance
            val initialCurrent = voltageLevel / (sqrt(3.0) * totalImpedance)

            resultDisplay.text = "Initial Three-Phase Short Circuit Current $initialCurrent"
        }
    }

    fun calculationThree() {
        setContentView(R.layout.layout3)
        val resultDisplay = findViewById<TextView>(R.id.result_display)
        val calculateButton = findViewById<Button>(R.id.button_calculate)
        val ratedPowerInput = findViewById<EditText>(R.id.input_rated_power)
        val ratedVoltageInput = findViewById<EditText>(R.id.input_rated_voltage)

        calculateButton.setOnClickListener {
            val maxShortCircuitVoltage = 11.1
            val ratedVoltage = ratedVoltageInput.text.toString().toDoubleOrNull() ?: 0.0
            val transformerRatedPower = ratedPowerInput.text.toString().toDoubleOrNull() ?: 0.0

            val transformerImpedance = (maxShortCircuitVoltage * ratedVoltage * ratedVoltage) / (100 * transformerRatedPower)
            val resistanceHigh = 10.65
            val reactanceHigh = 24.02
            val impedanceHigh = reactanceHigh + transformerImpedance
            val minResistance = 34.88
            val minReactance = 65.68
            val minImpedance = minReactance + transformerImpedance

            val highVoltage = 11.0
            val correctionFactor = highVoltage * highVoltage / (ratedVoltage * ratedVoltage)

            val correctedResistanceHigh = resistanceHigh * correctionFactor
            val correctedReactanceHigh = impedanceHigh * correctionFactor
            val correctedMinResistance = minResistance * correctionFactor
            val correctedMinImpedance = minImpedance * correctionFactor

            val lineLength = 12.37
            val lineResistance = 0.64
            val lineReactance = 0.363

            val lineResistanceTotal = lineLength * lineResistance
            val lineReactanceTotal = lineLength * lineReactance

            val systemResistance = lineResistanceTotal + correctedResistanceHigh
            val systemReactance = lineReactanceTotal + correctedReactanceHigh
            val totalImpedance = sqrt(systemResistance * systemResistance + systemReactance * systemReactance)

            val minSystemResistance = lineResistanceTotal + correctedMinResistance
            val minSystemReactance = lineReactanceTotal + correctedMinImpedance
            val minTotalImpedance = sqrt(minSystemResistance * minSystemResistance + minSystemReactance * minSystemReactance)

            val threePhaseCurrentNominal = highVoltage * 1000 / (sqrt(3.0) * totalImpedance)
            val twoPhaseCurrentNominal = threePhaseCurrentNominal * (sqrt(3.0) / 2)
            val threePhaseCurrentMin = highVoltage * 1000 / (sqrt(3.0) * minTotalImpedance)
            val twoPhaseCurrentMin = threePhaseCurrentMin * (sqrt(3.0) / 2)

            resultDisplay.text = "Three-Phase Nominal Current $threePhaseCurrentNominal, Two-Phase Nominal Current $twoPhaseCurrentNominal, Three-Phase Minimum Current $threePhaseCurrentMin, Two-Phase Minimum Current $twoPhaseCurrentMin"
        }
    }
}
