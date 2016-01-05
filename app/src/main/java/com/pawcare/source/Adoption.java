package com.pawcare.source;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.pawcare.source.util.DataFetchedCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anmumukh on 12/26/15.
 */
public class Adoption {
    public static final String ADOPTION_SIZE = "Size";
    public static final String ADOPTION_GENDER = "Gender";
    public static final String ADOPTION_VACCINATED = "Vaccinated";
    public static final String ADOPTION_BREED = "Breed";
    public static final String ADOPTION_IMAGE = "imageFile";
    public static final String ADOPTION_TYPE = "Type";
    public static final String ADOPTION_CREATED = "createdAt";
    public static final String ADOPTION_UPDATED = "updatedAt";
    public static final String ADOPTION_EMAIL = "email";
    public static final String ADOPTION_AGE = "Age";
    public static final String ADOPTION_ADDRESS = "Address";
    public static final String ADOPTION_TABLE = "Adoption";
    public static final String ADOPTION_IMAGE_NAME = "Adoption.jpg";

    private String size;
    private String gender;
    private boolean vaccinated;
    private String breed;
    private ParseFile imageFile;
    private Date created;
    private Date updated;
    private String email;
    private int age;
    private String address;
    private byte[] imageData;
    private String type;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public ParseFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(ParseFile imageFile) {
        this.imageFile = imageFile;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Adoption{" +
                "size='" + size + '\'' +
                ", gender='" + gender + '\'' +
                ", vaccinated=" + vaccinated +
                ", breed='" + breed + '\'' +
                ", imageFile=" + imageFile +
                ", created=" + created +
                ", updated=" + updated +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", imageData=" + Arrays.toString(imageData) +
                ", type='" + type + '\'' +
                '}';
    }

    public static void getInRange(int start, int limit, final DataFetchedCallback callback){
        final ArrayList adoptions = new ArrayList();
        ParseQuery query = new ParseQuery("Adoption");
        query.setSkip(start);
        query.setLimit(limit);
        query.orderByDescending(ADOPTION_CREATED);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    callback.dataFetched(null,e);
                } else {
                    for (ParseObject p : list) {
                        Adoption adoption = new Adoption();
                        adoption.size = p.getString(ADOPTION_SIZE);
                        adoption.vaccinated = p.getBoolean(ADOPTION_VACCINATED);
                        adoption.breed = p.getString(ADOPTION_BREED);
                        adoption.imageFile = p.getParseFile(ADOPTION_IMAGE);
                        adoption.created = p.getCreatedAt();
                        adoption.updated = p.getUpdatedAt();
                        adoption.email = p.getString(ADOPTION_EMAIL);
                        adoption.age = p.getNumber(ADOPTION_AGE).intValue();
                        adoption.address = p.getString(ADOPTION_ADDRESS);
                        adoption.gender = p.getString(ADOPTION_GENDER);
                        adoption.type = p.getString(ADOPTION_TYPE);
                        adoptions.add(adoption);
                    }
                    callback.dataFetched(adoptions,null);
                }
            }
        });
    }
    public static Adoption[] getInRange(int start, int limit){
        final ArrayList adoptions = new ArrayList();
        ParseQuery query = new ParseQuery("Adoption");
        query.setSkip(start);
        query.setLimit(limit);
        query.orderByDescending(ADOPTION_CREATED);
        try {
            List<ParseObject> list = query.find();
            for (ParseObject p : list) {
                Adoption adoption = new Adoption();
                adoption.size = p.getString(ADOPTION_SIZE);
                adoption.vaccinated = p.getBoolean(ADOPTION_VACCINATED);
                adoption.breed = p.getString(ADOPTION_BREED);
                adoption.imageFile = p.getParseFile(ADOPTION_IMAGE);
                adoption.created = p.getCreatedAt();
                adoption.updated = p.getUpdatedAt();
                adoption.email = p.getString(ADOPTION_EMAIL);
                adoption.age = p.getNumber(ADOPTION_AGE).intValue();
                adoption.address = p.getString(ADOPTION_ADDRESS);
                adoption.gender = p.getString(ADOPTION_GENDER);
                adoption.type = p.getString(ADOPTION_TYPE);
                adoptions.add(adoption);
            }
        }catch (Exception e){
            Log.d("PAWED",e.toString());
        }
        if (adoptions.size() > 0){
            Adoption [] adoptionArray = new Adoption[adoptions.size()];
//            adoptionArray = adoptions.toArray(adoptionArray);
            return adoptionArray;
        }
        return null;
    }
    public static Adoption[] getInRangeWithFilters(int start, int limit,HashMap filters){
        final ArrayList adoptions = new ArrayList();
        ParseQuery query = new ParseQuery("Adoption");
        query.setSkip(start);
        query.setLimit(limit);
        if (filters.get(ADOPTION_SIZE) != null){
            if (filters.get(ADOPTION_SIZE) instanceof List){
                query.whereContainedIn(ADOPTION_SIZE,(Collection)filters.get(ADOPTION_SIZE));
            }
            else if (filters.get(ADOPTION_SIZE) instanceof String){
                query.whereEqualTo(ADOPTION_SIZE,filters.get(ADOPTION_SIZE));
            }
        }
        if (filters.get(ADOPTION_VACCINATED) != null){
            if (filters.get(ADOPTION_VACCINATED) instanceof String){
                query.whereEqualTo(ADOPTION_VACCINATED, filters.get(ADOPTION_VACCINATED));
            }
        }
        if (filters.get(ADOPTION_BREED) != null){
            if (filters.get(ADOPTION_BREED) instanceof List){
                query.whereContainedIn(ADOPTION_BREED, (Collection) filters.get(ADOPTION_BREED));
            }
            else if (filters.get(ADOPTION_BREED) instanceof String){
                query.whereEqualTo(ADOPTION_BREED,filters.get(ADOPTION_BREED));
            }
        }
        if (filters.get(ADOPTION_SIZE) != null){
            if (filters.get(ADOPTION_SIZE) instanceof List){
                query.whereContainedIn(ADOPTION_SIZE, (Collection) filters.get(ADOPTION_SIZE));
            }
            else if (filters.get(ADOPTION_SIZE) instanceof String){
                query.whereEqualTo(ADOPTION_SIZE,filters.get(ADOPTION_SIZE));
            }
        }
        if (filters.get(ADOPTION_AGE) != null){
            if (filters.get(ADOPTION_AGE) instanceof List){

                query.whereGreaterThanOrEqualTo(ADOPTION_AGE,((List)filters.get(ADOPTION_AGE)).get(0));
                query.whereLessThanOrEqualTo(ADOPTION_AGE, ((List)filters.get(ADOPTION_AGE)).get(1));
            }
            else if (filters.get(ADOPTION_AGE) instanceof Number){
                query.whereEqualTo(ADOPTION_AGE,filters.get(ADOPTION_AGE));
            }
        }
        query.orderByDescending(ADOPTION_CREATED);
        try {
            List<ParseObject> list = query.find();
            for (ParseObject p : list) {
                Adoption adoption = new Adoption();
                adoption.size = p.getString(ADOPTION_SIZE);
                adoption.vaccinated = p.getBoolean(ADOPTION_VACCINATED);
                adoption.breed = p.getString(ADOPTION_BREED);
                adoption.imageFile = p.getParseFile(ADOPTION_IMAGE);
                adoption.created = p.getCreatedAt();
                adoption.updated = p.getUpdatedAt();
                adoption.email = p.getString(ADOPTION_EMAIL);
                adoption.age = p.getNumber(ADOPTION_AGE).intValue();
                adoption.address = p.getString(ADOPTION_ADDRESS);
                adoption.gender = p.getString(ADOPTION_GENDER);
                adoption.type = p.getString(ADOPTION_TYPE);
                adoptions.add(adoption);
            }
        }catch (Exception e){
            Log.d("PAWED",e.toString());
        }
        if (adoptions.size() > 0){
            Adoption [] adoptionArray = new Adoption[adoptions.size()];
//            adoptionArray = adoptions.toArray(adoptionArray);
            return adoptionArray;
        }
        return null;
    }
    //TODO Persist should get the current local user, get the relation from server and then persist
    public void persist(final PersistCallback callback){
        ParseObject adoption = new ParseObject(ADOPTION_TABLE);
        if (size != null){
            adoption.put(ADOPTION_SIZE,size);
        }
        adoption.put(ADOPTION_VACCINATED,vaccinated);
        if (breed != null){
            adoption.put(ADOPTION_BREED,breed);
        }
        if (email != null){
            adoption.put(ADOPTION_EMAIL,email);
        }
        adoption.put(ADOPTION_AGE, age);
        if (address != null){
            adoption.put(ADOPTION_EMAIL,address);
        }
        if (imageData != null){
            adoption.put(ADOPTION_IMAGE,new ParseFile(ADOPTION_IMAGE_NAME,imageData));
        }
        adoption.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (callback != null){
                    callback.persisted(e);
                }
            }
        });

    }
    public byte[] fetchImage(){
        if (imageData != null && imageData.length > 0){
            return imageData;
        }
        if (imageFile != null){
            try {
                imageData = imageFile.getData();
            }catch (Exception e){
                Log.d("PAWED",e.toString());
            }
        }
        return imageData.length > 0?imageData:null;
    }
    void fetchImage(final DataFetchedCallback callback){
        if (imageData != null && imageData.length > 0){
            ArrayList list = new ArrayList();
            list.add(imageData);
            if (callback != null){
                callback.dataFetched(list,null);
            }

        }
        if (imageFile != null){
            imageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (bytes.length > 0){
                        imageData = bytes;
                        ArrayList list = new ArrayList();
                        list.add(imageData);
                        if (callback != null){
                            callback.dataFetched(list,null);
                        }
                    }
                    else {
                        if (callback != null){
                            callback.dataFetched(null, e != null?e:new NullPointerException());
                        }
                    }
                }
            });
        }
    }
}
