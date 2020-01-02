package garg.sarthik.kpitadmin.Statics;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import garg.sarthik.kpitadmin.POJO.Admin;

public class Variables {


    public static FirebaseFirestore db;

    public static CollectionReference colLocations;
    public static CollectionReference colAdmins;
    public static FirebaseUser firebaseUser;

    public static Admin admin;


}
