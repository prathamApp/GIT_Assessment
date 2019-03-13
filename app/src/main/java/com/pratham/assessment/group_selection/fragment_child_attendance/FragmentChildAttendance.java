package com.pratham.assessment.group_selection.fragment_child_attendance;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.Assessment_Constants;
import com.pratham.assessment.Assessment_Utility;
import com.pratham.assessment.R;
import com.pratham.assessment.dao.StatusDao;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Student;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class FragmentChildAttendance extends Fragment implements ContractChildAttendance.attendanceView {

    @BindView(R.id.rv_child)
    RecyclerView rv_child;
    @BindView(R.id.btn_attendance_next)
    Button btn_attendance_next;
    /* @BindView(R.id.add_child)
     RelativeLayout add_child;
 */
    ChildAdapter childAdapter;
    ArrayList<Student> students;
    // ArrayList<String> avatars;
    ArrayList<Integer> avatars;
    private int revealX;
    private int revealY;
    private String groupID = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_attendance, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        students = getArguments().getParcelableArrayList(Assessment_Constants.STUDENT_LIST);
        avatars = new ArrayList<>();
        if (AssessmentApplication.isTablet) {
            btn_attendance_next.setVisibility(View.VISIBLE);
            //         add_child.setVisibility(View.GONE);
            groupID = getArguments().getString(Assessment_Constants.GROUPID);
            for (Student stu : students)
                avatars.add(Assessment_Utility.getRandomAvatar(getActivity()));
        } else {
            btn_attendance_next.setVisibility(View.GONE);
            //          add_child.setVisibility(View.VISIBLE);
            groupID = "SmartPhone";
            /*for (Student stu : students)
                avatars.add(stu.getAvatarName());*/
        }
        setChilds(students);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setChilds(ArrayList<Student> childs) {
        if (childAdapter == null) {
            childAdapter = new ChildAdapter(getActivity(), childs, avatars, FragmentChildAttendance.this);
            rv_child.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            rv_child.setLayoutManager(mLayoutManager);
            rv_child.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(15), true));
            rv_child.setItemAnimator(new DefaultItemAnimator());
            // rv_child.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            rv_child.setAdapter(childAdapter);
        } else {
            childAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void childItemClicked(Student student, int position) {
//        AssessmentApplication.bubble_mp.start();

        for (int i = 0; i < students.size(); i++)
            students.get(i).setChecked(false);
        students.get(position).setChecked(true);
        childAdapter.notifyDataSetChanged();

        Log.d("ooo", "" + position);
        /*for (Student stu : students) {
            if (stu.getStudentID().equalsIgnoreCase(student.getStudentID())) {
                if (stu.isChecked()) {
                    stu.setChecked(false);
                    //itemView.setCardBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                    //itemView.setBackgroundColor(getResources().getColor(R.color.colorAccent));


                } else {
                    stu.setChecked(true);
                    //itemView.setBackgroundColor(getResources().getColor(R.color.colorWhiteDark));

                    *//* itemView.setBackground(getResources().getDrawable(R.drawable.correct_bg));*//*

                }
                break;
            }
        }
        childAdapter.notifyDataSetChanged();*/
        // setChilds(students);
    }

    @Override
    public void moveToDashboardOnChildClick(Student student, int position, View v) {
        AssessmentApplication.bubble_mp.start();
        //      FastSave.getInstance().saveString(Assessment_Constants.AVATAR, student.getAvatarName());
        ArrayList<Student> s = new ArrayList<>();
        s.add(student);
        markAttendance(s);
        presentActivity(v);
    }

    @OnTouch(R.id.btn_attendance_next)
    public boolean setNextAvatar(View view, MotionEvent event) {
        revealX = (int) event.getRawX();
        revealY = (int) event.getY();
        return getActivity().onTouchEvent(event);
    }

   /* @OnTouch(R.id.rv_child)
    public boolean getRecyclerTouch(View view, MotionEvent event) {
        revealX = (int) event.getRawX();
        revealY = (int) event.getY();
        return getActivity().onTouchEvent(event);
    }*/

    @OnClick(R.id.btn_attendance_next)
    public void setNext(View v) {
        ArrayList<Student> checkedStds = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isChecked())
                checkedStds.add(students.get(i));
        }
        if (checkedStds.size() > 0) {
//            AssessmentApplication.bubble_mp.start();
            //FastSave.getInstance().saveString(Assessment_Constants.AVATAR, "avatars/dino_dance.json");
            // markAttendance(checkedStds);
            startSession(checkedStds);
            presentActivity(v);
            Toast.makeText(getActivity(), "Success...", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(getActivity(), ChooseLevelActivity.class));
        } else {
            Toast.makeText(getContext(), "Please Select Students !", Toast.LENGTH_SHORT).show();
        }
    }


    private void startSession(final ArrayList<Student> stud) {
        new AsyncTask<Object, Void, Object>() {
            String currentSession;

            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    StatusDao statusDao = AppDatabase.getDatabaseInstance(getContext()).getStatusDao();
                    currentSession = "" + UUID.randomUUID().toString();
                    Assessment_Constants.currentSession = currentSession;
                    statusDao.updateValue("CurrentSession", "" + currentSession);

                    String AppStartDateTime = AppDatabase.getDatabaseInstance(getContext()).getStatusDao().getValue("AppStartDateTime");

                    Session startSesion = new Session();
                    startSesion.setSessionID("" + currentSession);
                    String timerTime = AssessmentApplication.getCurrentDateTime(false, AppStartDateTime);

                    Log.d("doInBackground", "--------------------------------------------doInBackground : " + timerTime);
                    startSesion.setFromDate(timerTime);
                    startSesion.setToDate("NA");
                    startSesion.setSentFlag(0);
                    AppDatabase.getDatabaseInstance(getContext()).getSessionDao().insert(startSesion);

                    Attendance attendance = new Attendance();
                    for (int i = 0; i < stud.size(); i++) {
                        attendance.setSessionID("" + currentSession);
                        attendance.setStudentID("" + stud.get(i).getStudentID());
                        attendance.setDate(AssessmentApplication.getCurrentDateTime());
                        attendance.setGroupID(groupID);
                        attendance.setSentFlag(0);
                        AppDatabase.getDatabaseInstance(getContext()).getAttendanceDao().insert(attendance);
                    }

                    BackupDatabase.backup(getContext());
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }


    public void markAttendance(ArrayList<Student> stud) {
        //     FastSave.getInstance().saveString(Assessment_Constants.SESSIONID, COS_Utility.getUUID().toString());
        ArrayList<Attendance> attendances = new ArrayList<>();
        for (Student stu : stud) {
            Attendance attendance = new Attendance();
            //           attendance.setSessionID( FastSave.getInstance().getString(Assessment_Constants.SESSIONID, ""));
            attendance.setStudentID(stu.getStudentID());
            attendance.setDate(AssessmentApplication.getCurrentDateTime());
            attendance.GroupID = groupID;
            attendance.setSentFlag(0);
            //          FastSave.getInstance().saveString(Assessment_Constants.GROUPID, groupID);
            attendances.add(attendance);

            Assessment_Constants.currentStudentID = groupID;
        }
        //BaseActivity.attendanceDao.insertAttendance(attendances);
        AppDatabase.getDatabaseInstance(getActivity()).getAttendanceDao().insertAll(attendances);
        Session s = new Session();
        //      s.setSessionID(FastSave.getInstance().getString(Assessment_Constants.SESSIONID, ""));
        s.setFromDate(AssessmentApplication.getCurrentDateTime());
        s.setToDate("NA");
        AppDatabase.getDatabaseInstance(getActivity()).getSessionDao().insert(s);
//        BaseActivity.sessionDao.insert(s);
    }

    public void presentActivity(View view) {
       /* getActivity().startService(new Intent(getActivity(), AppKillService.class));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getActivity(), view, "transition");
        Intent intent = new Intent(getActivity(), ActivityMain.class);
        intent.putExtra(ActivityMain.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(ActivityMain.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        getActivity().finish();*/
    }

    /*@OnClick(R.id.add_child)
    public void setAdd_child() {
       *//* COS_Utility.showFragment(getActivity(), new Fragment_SelectAvatar(), R.id.frame_attendance,
                null, Fragment_SelectAvatar.class.getSimpleName());*//*

    }*/

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + avatar) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + avatar) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
