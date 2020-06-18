package com.pratham.atm.custom.LockNavigation

import android.app.Activity
import android.app.Application
import android.app.admin.DevicePolicyManager
import android.app.admin.SystemUpdatePolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.UserManager
import android.provider.Settings
import android.view.View
import android.widget.Toast


class PinActivity {

    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager
    private lateinit var activity: Activity
    private lateinit var applicationContext: Application
    private lateinit var packageName: String
    var isAdmin = false

    companion object {
        const val LOCK_ACTIVITY_KEY = "com.pratham.demo.MainActivityDemo"
    }

    fun init(activity: Activity, applicationContext: Application) {
        this.activity = activity
        this.applicationContext = applicationContext

        // super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        /*  setSupportActionBar(toolbar)
          val actionBar = supportActionBar
          actionBar!!.title = "App Kiosk"
          actionBar.elevation = 4.0F
          actionBar.setDisplayShowHomeEnabled(true)*/
        packageName = activity.packageName
        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(activity)
        mDevicePolicyManager = activity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        if (mDevicePolicyManager.isDeviceOwnerApp(packageName)) {
            Toast.makeText(applicationContext, "You are admin!", Toast.LENGTH_SHORT).show()
            isAdmin = true
        } else {
            Toast.makeText(applicationContext, "You are not the owner", Toast.LENGTH_SHORT).show()
        }
        setKioskPolicies(true, isAdmin)
        /*  btStartLockTask.setOnClickListener {
              setKioskPolicies(true, isAdmin)
          }

          btStopLockTask.setOnClickListener {

              setKioskPolicies(false, isAdmin)
              val intent = Intent(applicationContext, MainActivity::class.java).apply {
                  addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
              }
              intent.putExtra(LOCK_ACTIVITY_KEY, false)
              activity.startActivity(intent)
          }*/
    }

    fun unLockHomeButton(){
        setKioskPolicies(false, isAdmin)
        /*val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        intent.putExtra(LOCK_ACTIVITY_KEY, false)
        activity.startActivity(intent)*/
    }

    private fun setKioskPolicies(enable: Boolean, isAdmin: Boolean) {
       /* if (isAdmin) {
            // setRestrictions(enable)
            enableStayOnWhilePluggedIn(enable)
            setUpdatePolicy(enable)
            setAsHomeApp(enable)
            setKeyGuardEnabled(enable)
        }*/
        setLockTask(enable, isAdmin)
//        setImmersiveMode(enable)
    }

    private fun setRestrictions(disallow: Boolean) {
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow)
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow)
        setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow)
        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow)
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow)
    }

    private fun setUserRestriction(restriction: String, disallow: Boolean) = if (disallow) {
        mDevicePolicyManager.addUserRestriction(mAdminComponentName, restriction)
    } else {
        mDevicePolicyManager.clearUserRestriction(mAdminComponentName, restriction)
    }

    private fun enableStayOnWhilePluggedIn(active: Boolean) = if (active) {
        mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                Integer.toString(BatteryManager.BATTERY_PLUGGED_AC
                        or BatteryManager.BATTERY_PLUGGED_USB
                        or BatteryManager.BATTERY_PLUGGED_WIRELESS))
    } else {
        mDevicePolicyManager.setGlobalSetting(mAdminComponentName, Settings.Global.STAY_ON_WHILE_PLUGGED_IN, "0")
    }

    private fun setLockTask(start: Boolean, isAdmin: Boolean) {
        if (isAdmin) {
            mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, if (start) arrayOf(packageName) else arrayOf())
        }
        if (start) {
            activity.startLockTask()
        } else {
            activity.stopLockTask()
        }
    }

    private fun setUpdatePolicy(enable: Boolean) {
        if (enable) {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
                    SystemUpdatePolicy.createWindowedInstallPolicy(60, 120))
        } else {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName, null)
        }
    }

    private fun setAsHomeApp(enable: Boolean) {
        if (enable) {
            val intentFilter = IntentFilter(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addCategory(Intent.CATEGORY_DEFAULT)
            }
            mDevicePolicyManager.addPersistentPreferredActivity(
                    mAdminComponentName, intentFilter, ComponentName(packageName, PinActivity::class.java.name))
        } else {
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                    mAdminComponentName, packageName)
        }
    }

    private fun setKeyGuardEnabled(enable: Boolean) {
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, !enable)
    }

    private fun setImmersiveMode(enable: Boolean) {
        if (enable) {
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            activity.window.decorView.systemUiVisibility = flags
        } else {
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            activity.window.decorView.systemUiVisibility = flags
        }
    }
}