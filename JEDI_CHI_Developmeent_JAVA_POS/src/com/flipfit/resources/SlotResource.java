package com.flipfit.resources;

import com.flipfit.bean.Slot;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/api/slots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SlotResource {

    private final SessionFactory sessionFactory;

    public SlotResource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @POST
    public Response createSlot(Slot slot) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(slot);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(slot).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to create slot\"}")
                    .build();
        }
    }

    @GET
    public Response getAllSlots() {
        try {
            List<Slot> slots = getAllSlotsFromDB();
            return Response.ok(slots).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve slots\"}")
                    .build();
        }
    }

    @GET
    @Path("/{slotId}")
    public Response getSlotById(@PathParam("slotId") String slotId) {
        try {
            Optional<Slot> slot = getSlotByIdFromDB(slotId);
            if (slot.isPresent()) {
                return Response.ok(slot.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Slot not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve slot\"}")
                    .build();
        }
    }

    @GET
    @Path("/gym/{gymId}")
    public Response getSlotsByGym(@PathParam("gymId") String gymId) {
        try {
            List<Slot> slots = getSlotsByGymFromDB(gymId);
            return Response.ok(slots).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve gym slots\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{slotId}")
    public Response updateSlot(@PathParam("slotId") String slotId, Slot slot) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            slot.setSlotId(slotId);
            session.update(slot);
            session.getTransaction().commit();
            return Response.ok(slot).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to update slot\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{slotId}")
    public Response deleteSlot(@PathParam("slotId") String slotId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Slot slot = session.get(Slot.class, slotId);
            if (slot != null) {
                session.delete(slot);
                session.getTransaction().commit();
                return Response.noContent().build();
            } else {
                session.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Slot not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to delete slot\"}")
                    .build();
        }
    }

    // Private helper methods
    private List<Slot> getAllSlotsFromDB() {
        try (Session session = sessionFactory.openSession()) {
            Query<Slot> query = session.createQuery("FROM Slot", Slot.class);
            return query.list();
        }
    }

    private Optional<Slot> getSlotByIdFromDB(String slotId) {
        try (Session session = sessionFactory.openSession()) {
            Slot slot = session.get(Slot.class, slotId);
            return Optional.ofNullable(slot);
        }
    }

    private List<Slot> getSlotsByGymFromDB(String gymId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Slot> query = session.createQuery(
                "FROM Slot WHERE gymId = :gymId", Slot.class);
            query.setParameter("gymId", gymId);
            return query.list();
        }
    }
}
