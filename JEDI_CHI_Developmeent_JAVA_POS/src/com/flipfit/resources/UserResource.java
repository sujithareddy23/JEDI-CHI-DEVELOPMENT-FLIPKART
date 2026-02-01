package com.flipfit.resources;

import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymAdmin;
import com.flipfit.dto.LoginRequest;
import com.flipfit.dto.LoginResponse;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final SessionFactory sessionFactory;

    public UserResource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest loginRequest) {
        try {
            String role = authenticateUser(loginRequest.getIdentifier(), loginRequest.getPassword())
                    .orElseThrow(() -> new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED));
            
            String userId = getUserId(loginRequest.getIdentifier())
                    .orElseThrow(() -> new WebApplicationException("User ID not found", Response.Status.UNAUTHORIZED));
            
            // Check if role matches the requested role
            if (loginRequest.getRole() != null && !loginRequest.getRole().isEmpty()) {
                if (!role.equalsIgnoreCase(loginRequest.getRole())) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new LoginResponse(false, "Role mismatch"))
                            .build();
                }
            }
            
            LoginResponse response = new LoginResponse(true, "Login successful", userId, role);
            return Response.ok(response).build();
            
        } catch (WebApplicationException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new LoginResponse(false, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new LoginResponse(false, "Internal server error"))
                    .build();
        }
    }

    @POST
    @Path("/register/customer")
    public Response registerCustomer(GymCustomer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(customer);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(customer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to register customer\"}")
                    .build();
        }
    }

    @POST
    @Path("/register/owner")
    public Response registerOwner(GymOwner owner) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(owner);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(owner).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to register owner\"}")
                    .build();
        }
    }

    @POST
    @Path("/register/admin")
    public Response registerAdmin(GymAdmin admin) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(admin);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(admin).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to register admin\"}")
                    .build();
        }
    }

    private Optional<String> authenticateUser(String identifier, String password) {
        try (Session session = sessionFactory.openSession()) {
            // Try customer first
            Query<GymCustomer> customerQuery = session.createQuery(
                "FROM GymCustomer WHERE email = :email AND password = :password", 
                GymCustomer.class);
            customerQuery.setParameter("email", identifier);
            customerQuery.setParameter("password", password);
            
            GymCustomer customer = customerQuery.uniqueResult();
            if (customer != null) {
                return Optional.of("CUSTOMER");
            }

            // Try owner
            Query<GymOwner> ownerQuery = session.createQuery(
                "FROM GymOwner WHERE emailId = :email AND password = :password", 
                GymOwner.class);
            ownerQuery.setParameter("email", identifier);
            ownerQuery.setParameter("password", password);
            
            GymOwner owner = ownerQuery.uniqueResult();
            if (owner != null) {
                return Optional.of("OWNER");
            }

            // Try admin (using name as identifier since no email field)
            Query<GymAdmin> adminQuery = session.createQuery(
                "FROM GymAdmin WHERE name = :name AND password = :password", 
                GymAdmin.class);
            adminQuery.setParameter("name", identifier);
            adminQuery.setParameter("password", password);
            
            GymAdmin admin = adminQuery.uniqueResult();
            if (admin != null) {
                return Optional.of("ADMIN");
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private Optional<String> getUserId(String identifier) {
        try (Session session = sessionFactory.openSession()) {
            // Try customer first
            Query<GymCustomer> customerQuery = session.createQuery(
                "FROM GymCustomer WHERE email = :email", GymCustomer.class);
            customerQuery.setParameter("email", identifier);
            
            GymCustomer customer = customerQuery.uniqueResult();
            if (customer != null) {
                return Optional.of(customer.getId());
            }

            // Try owner
            Query<GymOwner> ownerQuery = session.createQuery(
                "FROM GymOwner WHERE emailId = :email", GymOwner.class);
            ownerQuery.setParameter("email", identifier);
            
            GymOwner owner = ownerQuery.uniqueResult();
            if (owner != null) {
                return Optional.of(owner.getId());
            }

            // Try admin (using name as identifier)
            Query<GymAdmin> adminQuery = session.createQuery(
                "FROM GymAdmin WHERE name = :name", GymAdmin.class);
            adminQuery.setParameter("name", identifier);
            
            GymAdmin admin = adminQuery.uniqueResult();
            if (admin != null) {
                return Optional.of(admin.getAdminId());
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user ID", e);
        }
    }
}
