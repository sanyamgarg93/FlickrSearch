package com.uber.flickrsearch.Network;

import org.json.JSONObject;

public interface HTTPApiCallInterface {
    public void notifySuccess(JSONObject response);
    public void notifyError(JSONObject error);
}
