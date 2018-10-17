package com.example.victor.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/******
 * Created by Victor on 8/18/2018.
 ******/
public class StepItem implements Parcelable {

    public static final Creator<StepItem> CREATOR = new Creator<StepItem>() {
        @Override
        public StepItem createFromParcel(Parcel in) {
            return new StepItem(in);
        }

        @Override
        public StepItem[] newArray(int size) {
            return new StepItem[size];
        }
    };

    private int stepId;
    private String stepShortDescription;
    private String stepDescription;
    private String stepVideoUrl;
    private String stepThumbnailUrl;

    public StepItem(int stepId, String stepShortDescription, String stepDescription, String stepVideoURL, String stepThumbnailUrl) {
        this.stepId = stepId;
        this.stepShortDescription = stepShortDescription;
        this.stepDescription = stepDescription;
        this.stepVideoUrl = stepVideoURL;
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    public int getStepId() {
        return stepId;
    }
    public String getStepShortDescription() { return stepShortDescription; }
    public String getStepDescription() {
        return stepDescription;
    }
    public String getStepVideoUrl() {
        return stepVideoUrl;
    }
    public String getStepThumbnailUrl() {
        return stepThumbnailUrl;
    }

    public StepItem(Parcel in) {
        stepId = in.readInt();
        stepShortDescription = in.readString();
        stepDescription = in.readString();
        stepVideoUrl = in.readString();
        stepThumbnailUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stepId);
        dest.writeString(stepShortDescription);
        dest.writeString(stepDescription);
        dest.writeString(stepVideoUrl);
        dest.writeString(stepThumbnailUrl);
    }
}
