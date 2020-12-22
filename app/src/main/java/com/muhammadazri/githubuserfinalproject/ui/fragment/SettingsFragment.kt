package com.muhammadazri.githubuserfinalproject.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import com.muhammadazri.githubuserfinalproject.R
import com.muhammadazri.githubuserfinalproject.utility.Reminder

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var preferenceType: SwitchPreference
    private lateinit var reminderNotif: Reminder
    private lateinit var reminder: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        reminderNotif = Reminder()

        initReminder()
        initPreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.settings)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == reminder) {
            if (sharedPreferences != null) {
                preferenceType.isChecked = sharedPreferences.getBoolean(reminder, false)
            }
        }

        val state: Boolean =
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(reminder, false)

        setReminder(state)
    }

    private fun initReminder(){
        reminder = getString(R.string.reminder_key)
        preferenceType = findPreference<SwitchPreference>(reminder)!!
    }


    private fun initPreference(){
        val sharedPreferences = preferenceManager.sharedPreferences
        preferenceType.isChecked = sharedPreferences.getBoolean(reminder, false)
    }

    private fun setReminder(state: Boolean) {
        if (state) {
            context?.let {
                reminderNotif.setRepeatingReminder(it)
            }
        } else {
            context?.let {
                reminderNotif.cancelAlarm(it)
            }
        }
    }
}