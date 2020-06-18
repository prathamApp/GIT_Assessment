package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Student")
public class Student implements Comparable, Parcelable {

    @PrimaryKey()
    @NonNull
    @SerializedName("StudentId")
    private String StudentID;
    @SerializedName("FullName")
    public String FullName;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("Age")
    private int Age;
    @SerializedName("Class")
    public String Stud_Class;
    @SerializedName("GroupId")
    public String GroupId;
    @SerializedName("GroupName")
    public String GroupName;
    @SerializedName("sentFlag")
    public int sentFlag;
    private String DeviceId;
    private String StudentUID;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String regDate;
    private String villageName;
    private int newFlag;
    public String avatarName;
    public String isniosstudent;


    @Ignore

    transient boolean isChecked = false;

    public Student(Parcel in) {
        GroupId = in.readString();
        GroupName = in.readString();
        FullName = in.readString();
        FirstName = in.readString();
        MiddleName = in.readString();
        LastName = in.readString();
        Stud_Class = in.readString();
        Age = in.readInt();
        Gender = in.readString();
        //     sentFlag = in.readInt();
        StudentID = in.readString();
        avatarName = in.readString();
        isChecked = in.readByte() != 0;
    }


    @Override
    public String toString() {
        return "Student{" +
                "StudentID='" + StudentID + '\'' +
                ", StudentUID='" + StudentUID + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", MiddleName='" + MiddleName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Gender='" + Gender + '\'' +
                ", regDate='" + regDate + '\'' +
                ", Age=" + Age +
                ", villageName='" + villageName + '\'' +
                ", newFlag=" + newFlag +
                ", DeviceId='" + DeviceId + '\'' +
                '}';
    }

    public Student() {
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getStud_Class() {
        return Stud_Class;
    }

    public void setStud_Class(String stud_Class) {
        Stud_Class = stud_Class;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    @NonNull
    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(@NonNull String studentID) {
        StudentID = studentID;
    }

    public String getStudentUID() {
        return StudentUID;
    }

    public void setStudentUID(String studentUID) {
        StudentUID = studentUID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public int getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(int newFlag) {
        this.newFlag = newFlag;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getIsniosstudent() {
        return isniosstudent;
    }

    public void setIsniosstudent(String isniosstudent) {
        this.isniosstudent = isniosstudent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GroupId);
        dest.writeString(GroupName);
        dest.writeString(FullName);
        dest.writeString(FirstName);
        dest.writeString(MiddleName);
        dest.writeString(LastName);
        dest.writeString(Stud_Class);
        dest.writeInt(Age);
        dest.writeString(Gender);
        //      dest.writeInt(sentFlag);
        dest.writeString(StudentID);
        //      dest.writeString(avatarName);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Student compare = (Student) o;
        if (compare.getStudentID() != null) {
            if (compare.isChecked() == this.isChecked())
                return 0;
            else return 1;
        } else {
            return 0;
        }
    }

}
