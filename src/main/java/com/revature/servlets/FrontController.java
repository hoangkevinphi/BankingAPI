package com.revature.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.implementations.AccountDAOImpl;
import com.revature.dao.implementations.AccountStatusDAOImpl;
import com.revature.dao.implementations.AccountTypeDAOImpl;
import com.revature.dao.implementations.RoleDAOImpl;
import com.revature.dao.implementations.UserAccountDAOImpl;
import com.revature.dao.implementations.UserDAOImpl;
import com.revature.dao.interfaces.GenericDAO;
import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.AccountType;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.services.InterestService;
import com.revature.services.PasswordService;
import com.revature.services.ServerMessage;
import com.revature.services.StringUtils;

public class FrontController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static ObjectMapper objectMapper = new ObjectMapper();
	private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private static GenericDAO<Account> accountDAO = new AccountDAOImpl();
	private static GenericDAO<AccountStatus> accountStatusDAO = new AccountStatusDAOImpl();
	private static GenericDAO<AccountType> accountTypeDAO = new AccountTypeDAOImpl();
	private static GenericDAO<User> userDAO = new UserDAOImpl();
	private static GenericDAO<Role> roleDAO = new RoleDAOImpl();
	private static GenericDAO<UserAccount> userAccountDAO = new UserAccountDAOImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		String[] parts = path.split("/");
		if(parts.length == 3) {
			switch(parts[2].toLowerCase()) {
			case "users":
				usersGet(request,response);
				break;
			case "accounts":
				accountsGet(request,response);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
				break;
			}
		}else if(parts.length == 4){
			if(StringUtils.isInteger(parts[3])) {
				switch(parts[2].toLowerCase()) {
				case "users":
					usersGetById(request,response,Integer.parseInt(parts[3]));
					break;
				case "accounts":
					accountsGetById(request,response,Integer.parseInt(parts[3]));
					break;
				default:
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
					break;
				}
			}else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
			}
		}else if(parts.length == 5){
			if(parts[2].toLowerCase().equals("accounts")) {
				if(StringUtils.isInteger(parts[4])) {
					switch(parts[3].toLowerCase()) {
					case "status":
						accountsGetByStatusId(request,response,Integer.parseInt(parts[4]));
						break;
					case "owner":
						if(request.getQueryString()==null)
							accountsGetByUserId(request,response,Integer.parseInt(parts[4]));
						else {
							String[] queryParameters = request.getQueryString().split("=");
							if(queryParameters[0].toLowerCase().equals("accounttype")) {
								switch(queryParameters[queryParameters.length-1].toLowerCase()) {
								case "checkings":
									accountsGetByUserIdByType(request,response,Integer.parseInt(parts[4]), 1);
									break;
								case "savings":
									accountsGetByUserIdByType(request,response,Integer.parseInt(parts[4]), 2);
									break;
								default:
									response.setStatus(HttpServletResponse.SC_NOT_FOUND);
									response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
									break;
								}	
							}else {
								response.setStatus(HttpServletResponse.SC_NOT_FOUND);
								response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
							}
						}
						break;
					default:
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
						break;
					}
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
				}
			}else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		String[] parts = path.split("/");
		
		if(parts.length == 3) {
			switch(parts[2].toLowerCase()) {
			case "login":
				loginPost(request,response);
				break;
			case "logout":
				logoutPost(request,response);
				break;
			case "register":
				registerPost(request,response);
				break;
			case "accounts":
				accountsPost(request,response);
				break;
			case "passtime":
				accountsPassTime(request,response);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
				break;
			}
		}else if(parts.length == 4){
			if(parts[2].toLowerCase().equals("accounts")) {
			switch(parts[3].toLowerCase()) {
				case "withdraw":
					accountsWithdraw(request,response);
					break;
				case "deposit":
					accountsDeposit(request,response);
					break;
				case "transfer":
					accountsTransfer(request,response);
					break;
				default:
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
					break;
				}
			}else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
			}
		}else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		String[] parts = path.split("/");
		
		if(parts.length == 3) {
			switch(parts[2].toLowerCase()) {
			case "users":
				usersPut(request,response);
				break;
			case "accounts":
				accountsPut(request,response);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
				break;
			}
		}else if(parts.length == 5) {
			if(parts[2].toLowerCase().equals("users")) {
				if(StringUtils.isInteger(parts[4])) {
					switch(parts[3].toLowerCase()) {
					case "upgrade":
						usersUpgrade(request,response,Integer.parseInt(parts[4]));
						break;
					default:
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
						break;
					}
				}else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
				}
			}else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
			}
		}else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		String[] parts = path.split("/");
		
		if(parts.length == 5) {
			if(StringUtils.isInteger(parts[4])) {
				if(parts[3].toLowerCase().equals("delete")) {
					switch(parts[2]) {
					case "users":
						usersDelete(request,response,Integer.parseInt(parts[4]));
						break;
					case "accounts":
						accountsDelete(request,response,Integer.parseInt(parts[4]));
						break;
					default:
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
						break;
					}
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
				}
			}else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
			}
		}else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Resource not found")));
		}
	}
	
	void loginPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			User user;
			if((user = (User) request.getSession().getAttribute("current")) != null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("A user is already logged in")));
			}else {
				JsonNode jsonNode = objectMapper.readTree(request.getReader());
				user = null;
				for(User u : userDAO.getAll()) {
					if(u.getUsername().toLowerCase().equals(jsonNode.get("username").asText().toLowerCase())) {
							user = u;
							break;
					}
				}
				if(user!=null) {
					if(PasswordService.checkPassword(jsonNode.get("password").asText(), user.getPassword())) {
						request.getSession().setAttribute("current", user);
						response.setStatus(HttpServletResponse.SC_CREATED);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Successfully logged into " + user.getUsername())));
					}else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Invalid credentials")));
					}
				}else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Invalid credentials")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void logoutPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			User user;
			if((user = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("There was no user logged into the session")));
			} else {
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("You have successfully logged out " + user.getUsername())));
				request.getSession().setAttribute("current", null);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	void registerPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1) {
					JsonNode jsonNode = objectMapper.readTree(request.getReader());
					boolean invalid=false;
					for(User u : userDAO.getAll()) {
						if(	u.getUsername().toLowerCase().equals(jsonNode.get("username").asText().toLowerCase()) ||
							u.getEmail().toLowerCase().equals(jsonNode.get("email").asText().toLowerCase())) {
							invalid=true;
							break;
						}
					}
					if(!invalid) {
						User user = userDAO.create(new User(jsonNode.get("username").asText(),
															PasswordService.hashPassword(jsonNode.get("password").asText()),
															jsonNode.get("firstName").asText(),
															jsonNode.get("lastName").asText(),
															jsonNode.get("email").asText(),
															objectMapper.convertValue(jsonNode.get("role"), Role.class)));
						response.setStatus(HttpServletResponse.SC_CREATED);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
					}else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Invalid fields")));
					}
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void usersGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2) {
					List<User> users = userDAO.getAll();
					if(users.size() > 0)
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(users));
					else
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void usersGetById(HttpServletRequest request, HttpServletResponse response, int userId) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2 ||
				   current.getUserId() == userId) {
					User user = userDAO.get(userId);
					if(user != null)
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
					else
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void usersPut(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode jsonNode = objectMapper.readTree(request.getReader());
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2 ||
				   current.getUserId() == jsonNode.get("userId").asInt()) {
					User user = userDAO.update(new User(jsonNode.get("userId").asInt(),
														jsonNode.get("username").asText(),
														PasswordService.hashPassword(jsonNode.get("password").asText()),
														jsonNode.get("firstName").asText(),
														jsonNode.get("lastName").asText(),
														jsonNode.get("email").asText(),
														objectMapper.convertValue(jsonNode.get("role"), Role.class)));
					
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	void accountsPut(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode jsonNode = objectMapper.readTree(request.getReader());
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1) {
					Account account = accountDAO.update(new Account(jsonNode.get("accountId").asInt(),
																	Double.parseDouble(decimalFormat.format(jsonNode.get("balance").asDouble())),
																	accountStatusDAO.get(objectMapper.convertValue(jsonNode.get("status"), AccountStatus.class).getStatusId()),
																	accountTypeDAO.get(objectMapper.convertValue(jsonNode.get("type"), AccountType.class).getTypeId())));
					
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(account));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void accountsPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode jsonNode = objectMapper.readTree(request.getReader());
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2 ||
				   current.getUserId() == jsonNode.get("ownerId").asInt()) {
					Account account = accountDAO.create(new Account(Double.parseDouble(decimalFormat.format(jsonNode.get("balance").asDouble())),
																	objectMapper.convertValue(jsonNode.get("status"), AccountStatus.class),
																	objectMapper.convertValue(jsonNode.get("type"), AccountType.class)));
					userAccountDAO.create(new UserAccount(userDAO.get(jsonNode.get("ownerId").asInt()), account));
					response.setStatus(HttpServletResponse.SC_CREATED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(account));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void accountsWithdraw(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode jsonNode = objectMapper.readTree(request.getReader());
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getUserId() == userAccountDAO.get(jsonNode.get("accountId").asInt()).getUser().getUserId()) {
					if(jsonNode.get("amount").asDouble()>0) {
						Account account;
						if((account = accountDAO.get(jsonNode.get("accountId").asInt()))!= null) {
							Account updatedAccount = new Account(account.getAccountId(),
																 Double.parseDouble(decimalFormat.format(account.getBalance() - jsonNode.get("amount").asDouble())), 
																 account.getStatus(),
																 account.getType());
							accountDAO.update(updatedAccount);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("$ " + decimalFormat.format(jsonNode.get("amount").asDouble()) + " has been withdrawn from Account #" + account.getAccountId())));
						} else {
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Account does not exist")));
						}
					}else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Must withdraw a positive value")));
					}
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	void accountsDeposit(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode jsonNode = objectMapper.readTree(request.getReader());
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getUserId() == userAccountDAO.get(jsonNode.get("accountId").asInt()).getUser().getUserId()) {
					if(jsonNode.get("amount").asDouble()>0) {
						Account account;
						if((account = accountDAO.get(jsonNode.get("accountId").asInt()))!= null) {
							Account updatedAccount = new Account(account.getAccountId(),
																 Double.parseDouble(decimalFormat.format(account.getBalance() + jsonNode.get("amount").asDouble())), 
																 account.getStatus(),
																 account.getType());
							accountDAO.update(updatedAccount);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("$ " + decimalFormat.format(jsonNode.get("amount").asDouble()) + " has been deposited to Account #" + account.getAccountId())));
						} else {
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Account does not exist")));
						}
					}else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Must deposit a positive value")));
					}
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	void accountsTransfer(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode jsonNode = objectMapper.readTree(request.getReader());
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getUserId() == userAccountDAO.get(jsonNode.get("sourceAccountId").asInt()).getUser().getUserId()) {
					if(jsonNode.get("amount").asDouble()>0) {
						Account sourceAccount;
						Account targetAccount;
						if((sourceAccount = accountDAO.get(jsonNode.get("sourceAccountId").asInt()))!= null &&
						   (targetAccount = accountDAO.get(jsonNode.get("targetAccountId").asInt()))!= null) {
							if(sourceAccount.getAccountId() != targetAccount.getAccountId()) {
								Account updatedSourceAccount = new Account(sourceAccount.getAccountId(),
																		   Double.parseDouble(decimalFormat.format(sourceAccount.getBalance() - jsonNode.get("amount").asDouble())), 
																		   sourceAccount.getStatus(),
																		   sourceAccount.getType());
								
								Account updatedTargetAccount = new Account(targetAccount.getAccountId(),
																		   Double.parseDouble(decimalFormat.format(targetAccount.getBalance() + jsonNode.get("amount").asDouble())), 
																		   targetAccount.getStatus(),
																		   targetAccount.getType());
								accountDAO.update(updatedSourceAccount);
								accountDAO.update(updatedTargetAccount);
							}else {
								Account updatedAccount = new Account(sourceAccount.getAccountId(),
										   							 Double.parseDouble(decimalFormat.format(sourceAccount.getBalance())),
										   							 sourceAccount.getStatus(),
										   							 sourceAccount.getType());
								accountDAO.update(updatedAccount);
							}
							
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("$ " + decimalFormat.format(jsonNode.get("amount").asDouble()) + " has been transferred from Account#" + sourceAccount.getAccountId() + " to Account #" + targetAccount.getAccountId())));
						} else {
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Account does not exist")));
						}
					}else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Must transfer a positive value")));
					}
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void accountsGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2) {
					List<Account> accounts = accountDAO.getAll();
					if(accounts.size() > 0)
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts));
					else
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void accountsGetById(HttpServletRequest request, HttpServletResponse response, int accountId) {
		try {
			Account account = accountDAO.get(accountId);
			if(account != null) {
				User owner = userAccountDAO.get(accountId).getUser();
				User current;
				if((current = (User) request.getSession().getAttribute("current")) == null) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}else {
					if(current.getRole().getRoleId() == 1 ||
					   current.getRole().getRoleId() == 2 ||
					   current.getUserId() == owner.getUserId()) {
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(account));
					}else {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
					}
				}
			}else {
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void accountsGetByStatusId(HttpServletRequest request, HttpServletResponse response, int statusId) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2) {
					List<Account> accounts = accountDAO.getAccountsByStatusId(statusId);
					if(accounts.size() > 0)
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts));
					else
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void accountsGetByUserId(HttpServletRequest request, HttpServletResponse response, int userId) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2 ||
				   current.getUserId() == userId) {
					List<Account> accounts = accountDAO.getAccountsByUserId(userId);
					if(accounts.size() > 0)
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountDAO.getAccountsByUserId(userId)));
					else
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Stretch
	void accountsGetByUserIdByType(HttpServletRequest request, HttpServletResponse response, int userId, int typeId) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getRole().getRoleId() == 2 ||
				   current.getUserId() == userId) {
					List<Account> accounts = accountDAO.getAccountsByUserIdAndTypeId(userId, typeId);
					if(accounts.size() > 0)
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts));
					else
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void accountsPassTime(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode jsonNode = objectMapper.readTree(request.getReader());
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1) {
					for(Account a : accountDAO.getAccountsByType(2)) {
						Account account = new Account(a.getAccountId(),
													  Double.parseDouble(decimalFormat.format(InterestService.AccrueInterest(a.getBalance(), jsonNode.get("numMonths").asInt()))),
													  a.getStatus(),
													  a.getType());
						accountDAO.update(account);
					}
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage(jsonNode.get("numMonths").asInt() + " months of interest has been accrued for all Savings Accounts")));
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void usersUpgrade(HttpServletRequest request, HttpServletResponse response, int userId) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1 ||
				   current.getUserId() == userId) {
					User user = userDAO.get(userId);
					if(user == null) {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
					}else {
						if(user.getRole().getRoleId() == 3) {
							Account account=null;
							for(Account a : accountDAO.getAccountsByUserId(userId)) {
								if(a.getType().getTypeId() == 1) {
									account = a;
								}
							}
							if(account==null) {
								response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
								response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("User does not have a Checkings account to pay for the upgrade fee")));
							}else {
								double cost = 500.0;
								account = accountDAO.update(new Account(account.getAccountId(),
																		account.getBalance() - cost,
																		account.getStatus(),
																		account.getType()));
								
								user = userDAO.update(new User(user.getUserId(),
															   user.getUsername(),
															   user.getPassword(),
															   user.getFirstName(),
															   user.getLastName(),
															   user.getEmail(),
															   roleDAO.get(4)));
								
								response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage(user.getUsername() + " has been upgraded to Premium, $500 has been deducted from Account #" + account.getAccountId())));
							}
						}else {
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("User is not eligible for upgrade")));
						}
					}
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void usersDelete(HttpServletRequest request, HttpServletResponse response, int userId) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1) {
					User user = userDAO.get(userId);
					if(user == null) {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
					}else {
						boolean valid=true;
						for(UserAccount ua : userAccountDAO.getAll()) {
							if(ua.getUser().getUserId() == user.getUserId()) {
								valid=false;
								break;
							}
						}
						if(valid) {
							userDAO.delete(user);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
						}else {
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("User #" + user.getUserId() + " has one or more existing accounts. Please delete the account(s) before trying again")));
						}
					}
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void accountsDelete(HttpServletRequest request, HttpServletResponse response, int accountId) {
		try {
			User current;
			if((current = (User) request.getSession().getAttribute("current")) == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
			}else {
				if(current.getRole().getRoleId() == 1) {
					Account account = accountDAO.get(accountId);
					if(account == null) {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("No results were found")));
					}else {
						accountDAO.delete(account);
						userAccountDAO.delete(new UserAccount(null,account));
						response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("Account #" + account.getAccountId() + " has been deleted")));
					}
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ServerMessage("The requested action is not permitted")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
