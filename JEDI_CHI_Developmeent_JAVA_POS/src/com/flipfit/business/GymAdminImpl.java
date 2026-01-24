package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.data.DataStore;

import java.util.ArrayList;
import java.util.List;

public class GymAdminImpl implements GymAdminInterface {

    @Override
    public void validateGym(String gymId) {
        GymCenter g = DataStore.getCenters().get(gymId);
        if (g != null) g.setValidated(true);
        GymOwner o = ownerById(g != null ? g.getOwnerId() : null);
        if (o != null) o.setValidated(true);
    }

    @Override
    public void withdrawPermission(String gymId) {
        GymCenter g = DataStore.getCenters().get(gymId);
        if (g != null) g.setValidated(false);
    }

    @Override
    public List<GymOwner> listPendingPartners() {
        List<GymOwner> out = new ArrayList<>();
        for (GymOwner o : DataStore.getOwners().values()) {
            if (!o.isValidated()) out.add(o);
        }
        return out;
    }

    @Override
    public List<GymOwner> listVerifiedPartners() {
        List<GymOwner> out = new ArrayList<>();
        for (GymOwner o : DataStore.getOwners().values()) {
            if (o.isValidated()) out.add(o);
        }
        return out;
    }

    @Override
    public List<GymCenter> listPendingGymCenters() {
        List<GymCenter> out = new ArrayList<>();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (!g.isValidated()) out.add(g);
        }
        return out;
    }

    @Override
    public List<GymCenter> listVerifiedGymCenters() {
        List<GymCenter> out = new ArrayList<>();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (g.isValidated()) out.add(g);
        }
        return out;
    }

    @Override
    public List<GymCenter> searchGymsByLocation(String location) {
        if (location == null) return new ArrayList<>();
        List<GymCenter> out = new ArrayList<>();
        String l = location.toLowerCase().trim();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (g.getLocation() != null && g.getLocation().toLowerCase().contains(l)) {
                out.add(g);
            }
        }
        return out;
    }

    private GymOwner ownerById(String ownerId) {
        for (GymOwner o : DataStore.getOwners().values()) {
            if (ownerId != null && ownerId.equals(o.getId())) return o;
        }
        return null;
    }
}
