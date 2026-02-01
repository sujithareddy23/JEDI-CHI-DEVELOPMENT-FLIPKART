package com.flipfit.bean;

import javax.persistence.*;

@Entity
@Table(name = "gym_owners")
public class GymOwner {
    @Id
    @Column(name = "id")
    private String id;
    
    @Column(name = "owner_name")
    private String ownerName;
    
    @Column(name = "email_id")
    private String emailId;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "id_proof")
    private String idProof;
    
    @Column(name = "pan_no")
    private String panNo;
    
    @Column(name = "gst_no")
    private String gstNo;
    
    @Column(name = "validated")
    private boolean validated;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getIdProof() { return idProof; }
    public void setIdProof(String idProof) { this.idProof = idProof; }

    public String getPanNo() { return panNo; }
    public void setPanNo(String panNo) { this.panNo = panNo; }

    public String getGstNo() { return gstNo; }
    public void setGstNo(String gstNo) { this.gstNo = gstNo; }

    public boolean isValidated() { return validated; }
    public void setValidated(boolean validated) { this.validated = validated; }
}
