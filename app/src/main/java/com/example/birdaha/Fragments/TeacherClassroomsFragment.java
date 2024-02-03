package com.example.birdaha.Fragments;

import android.content.Intent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.birdaha.Activities.ClassroomScreen;

import com.example.birdaha.Adapters.TeacherClassroomsAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.R;
import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Utilities.TeacherClassroomsRecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherClassroomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherClassroomsFragment extends Fragment implements TeacherClassroomsRecyclerViewInterface {

    private RecyclerView teacherClassroomsRecyclerView;
    private List<Classroom> teacherClassrooms;
    private Teacher teacher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherClassroomsFragment() {
        // Required empty public constructor
    }

    public TeacherClassroomsFragment(Teacher teacher,List<Classroom> classrooms){
        this.teacher = teacher;
        this.teacherClassrooms = classrooms;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeacherClassroomsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherClassroomsFragment newInstance(String param1, String param2) {
        TeacherClassroomsFragment fragment = new TeacherClassroomsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_classrooms, container, false);
        teacherClassroomsRecyclerView = view.findViewById(R.id.teacher_classrooms_recyclerview);
        teacherClassroomsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        TeacherClassroomsAdapter adapter = new TeacherClassroomsAdapter(getActivity(), teacherClassrooms,this,teacher);
        teacherClassroomsRecyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Callback method invoked when a teacher classroom item is clicked.
     *
     * @param position The position of the clicked item in the RecyclerView.
     * @param view     The View that was clicked.
     */
    @Override
    public void onTeacherClassroomsItemClick(int position, View view, Teacher teacher) {
        Classroom current = teacherClassrooms.get(position);

        Intent intent = new Intent(requireActivity(), ClassroomScreen.class);
        intent.putExtra("classroom",current);
        intent.putExtra("teacher",teacher);
        startActivity(intent);

    }
}