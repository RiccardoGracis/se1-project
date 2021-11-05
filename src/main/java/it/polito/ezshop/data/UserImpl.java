package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidRoleException;


public class UserImpl implements User {
	
	private Integer id;
	private String username;
	private String password;
	private String role;
	
	public UserImpl(Integer id, String username, String password, String role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getRole() {
		return role;
	}
	
	@Override
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isAdministator() {
		return this.role.equals("Administrator");
	}
	
	public boolean isShopManager() {
		return this.role.equals("ShopManager");
	}
	
	public boolean isCashier() {
		return this.role.equals("Cashier");
	}

	public void updateRole(String role) throws InvalidRoleException {
		if (role == null || role.isEmpty()
				|| (!role.equals("Administrator") && !role.equals("ShopManager") && !role.equals("Cashier")))
			throw new InvalidRoleException();
		this.role = role;
	}

}
