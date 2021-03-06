package com.tf.base.basemanage.domain;

import java.util.Date;
import javax.persistence.*;

import com.tf.base.common.annotation.LogShowName;
import com.tf.base.common.constants.CommonConstants;

@Table(name = "party_org_info")
public class PartyOrgInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 党组织名称
     */
    @Column(name = "party_org_name")
    private String name;

    /**
     * 党组织类型
     */
    @LogShowName(value="党组织类型",dmm=CommonConstants.PARTY_ORG_CLASS)
    @Column(name = "party_org_type")
    private String partyOrgType;
    
    /**
     * 党组织成立时间
     */
    @Column(name = "party_set_up_time")
    private Date partySetUpTime;
    
    /**
     * 本届党组织开始时间
     */
    @Column(name = "party_start_time")
    private Date partyStartTime;
    
    /**
     * 上级党组织
     */
    @Column(name = "parent_party_org")
    private Integer parentPartyOrg;
    
    /**
     * 创建人
     */
    @LogShowName(value="创建人")
    private String creater;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
    
    /**
     * 创建单位
     */
    @Column(name = "create_org")
    private String createOrg;
    
    /**
     * 状态 1.是 0.否
     */
    private String status;
    
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPartyOrgType() {
		return partyOrgType;
	}



	public void setPartyOrgType(String partyOrgType) {
		this.partyOrgType = partyOrgType;
	}



	public Date getPartySetUpTime() {
		return partySetUpTime;
	}



	public void setPartySetUpTime(Date partySetUpTime) {
		this.partySetUpTime = partySetUpTime;
	}



	public Date getPartyStartTime() {
		return partyStartTime;
	}



	public Integer getParentPartyOrg() {
		return parentPartyOrg;
	}

	public void setParentPartyOrg(Integer parentPartyOrg) {
		this.parentPartyOrg = parentPartyOrg;
	}

	public void setPartyStartTime(Date partyStartTime) {
		this.partyStartTime = partyStartTime;
	}


	

	public String getCreater() {
		return creater;
	}



	public void setCreater(String creater) {
		this.creater = creater;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", partyOrgType=").append(partyOrgType);
        sb.append(", partySetUpTime=").append(partySetUpTime);
        sb.append(", partyStartTime=").append(partyStartTime);
        sb.append(", parentPartyOrg=").append(parentPartyOrg);
        sb.append(", creater=").append(creater);
        sb.append(", createTime=").append(createTime);
        sb.append(", creater=").append(creater);
        sb.append(", createTime=").append(createTime);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}