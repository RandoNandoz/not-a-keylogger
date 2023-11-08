package persistence;

import org.json.JSONObject;

import model.InputTime;

class FakeClass extends InputTime {

    @Override
    public JSONObject toJson() {
        return null;
    }
}