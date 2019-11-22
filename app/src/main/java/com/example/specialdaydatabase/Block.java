package com.example.specialdaydatabase;

//Tian Yu  Version: 1.0
//A object so it can be added to database more easily.
public class Block {
        public String startTime;
        public String endTime;
        public String periodName;
        public Block() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }
        public Block(String startTime, String endTime,String periodName) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.periodName=periodName;
        }

    }