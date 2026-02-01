package com.flipfit.resources;

import com.flipfit.bean.Booking;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Path("/api/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private final SessionFactory sessionFactory;

    public BookingResource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @POST
    public Response createBooking(Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(booking);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(booking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to create booking\"}")
                    .build();
        }
    }

    @GET
    public Response getAllBookings() {
        try {
            List<Booking> bookings = getAllBookingsFromDB();
            return Response.ok(bookings).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve bookings\"}")
                    .build();
        }
    }

    @GET
    @Path("/{bookingId}")
    public Response getBookingById(@PathParam("bookingId") String bookingId) {
        try {
            Optional<Booking> booking = getBookingByIdFromDB(bookingId);
            if (booking.isPresent()) {
                return Response.ok(booking.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Booking not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve booking\"}")
                    .build();
        }
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getBookingsByCustomer(@PathParam("customerId") String customerId) {
        try {
            List<Booking> bookings = getBookingsByCustomerFromDB(customerId);
            return Response.ok(bookings).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve customer bookings\"}")
                    .build();
        }
    }

    @GET
    @Path("/slot/{slotId}")
    public Response getBookingsBySlot(@PathParam("slotId") String slotId) {
        try {
            List<Booking> bookings = getBookingsBySlotFromDB(slotId);
            return Response.ok(bookings).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve slot bookings\"}")
                    .build();
        }
    }

    @GET
    @Path("/gym/{gymId}")
    public Response getBookingsByGym(@PathParam("gymId") String gymId) {
        try {
            List<Booking> bookings = getBookingsByGymFromDB(gymId);
            return Response.ok(bookings).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve gym bookings\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{bookingId}")
    public Response updateBooking(@PathParam("bookingId") String bookingId, Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            booking.setId(bookingId);
            session.update(booking);
            session.getTransaction().commit();
            return Response.ok(booking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to update booking\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{bookingId}")
    public Response deleteBooking(@PathParam("bookingId") String bookingId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                session.delete(booking);
                session.getTransaction().commit();
                return Response.noContent().build();
            } else {
                session.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Booking not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to delete booking\"}")
                    .build();
        }
    }

    // Private helper methods
    private List<Booking> getAllBookingsFromDB() {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> query = session.createQuery("FROM Booking", Booking.class);
            return query.list();
        }
    }

    private Optional<Booking> getBookingByIdFromDB(String bookingId) {
        try (Session session = sessionFactory.openSession()) {
            Booking booking = session.get(Booking.class, bookingId);
            return Optional.ofNullable(booking);
        }
    }

    private List<Booking> getBookingsByCustomerFromDB(String customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> query = session.createQuery(
                "FROM Booking WHERE customerId = :customerId", Booking.class);
            query.setParameter("customerId", customerId);
            return query.list();
        }
    }

    private List<Booking> getBookingsBySlotFromDB(String slotId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> query = session.createQuery(
                "FROM Booking WHERE slotId = :slotId", Booking.class);
            query.setParameter("slotId", slotId);
            return query.list();
        }
    }

    private List<Booking> getBookingsByGymFromDB(String gymId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> query = session.createQuery(
                "FROM Booking WHERE gymId = :gymId", Booking.class);
            query.setParameter("gymId", gymId);
            return query.list();
        }
    }
}
