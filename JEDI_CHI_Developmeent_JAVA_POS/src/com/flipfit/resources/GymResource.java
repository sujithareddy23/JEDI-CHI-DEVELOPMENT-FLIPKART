package com.flipfit.resources;

import com.flipfit.bean.GymCenter;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/gyms")
@Produces(MediaType.APPLICATION_JSON)
public class GymResource {

    private final SessionFactory sessionFactory;

    public GymResource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @POST
    public Response createGym(GymCenter gym) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(gym);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(gym).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to create gym\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{gymId}")
    public Response updateGym(@PathParam("gymId") String gymId, GymCenter gym) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            gym.setGymId(gymId);
            session.update(gym);
            session.getTransaction().commit();
            return Response.ok(gym).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to update gym\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{gymId}")
    public Response deleteGym(@PathParam("gymId") String gymId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            GymCenter gym = session.get(GymCenter.class, gymId);
            if (gym != null) {
                session.delete(gym);
                session.getTransaction().commit();
                return Response.noContent().build();
            } else {
                session.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Gym not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to delete gym\"}")
                    .build();
        }
    }

    @GET
    public Response getAllGyms() {
        try {
            List<GymCenter> gyms = getAllGymsFromDB();
            return Response.ok(gyms).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve gyms\"}")
                    .build();
        }
    }

    @GET
    @Path("/{gymId}")
    public Response getGymById(@PathParam("gymId") String gymId) {
        try {
            GymCenter gym = getGymByIdFromDB(gymId);
            if (gym != null) {
                return Response.ok(gym).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Gym not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve gym\"}")
                    .build();
        }
    }

    private List<GymCenter> getAllGymsFromDB() {
        try (Session session = sessionFactory.openSession()) {
            Query<GymCenter> query = session.createQuery("FROM GymCenter", GymCenter.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve gyms", e);
        }
    }

    private GymCenter getGymByIdFromDB(String gymId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(GymCenter.class, gymId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve gym", e);
        }
    }
}
