package com.flipfit.resources;

import com.flipfit.bean.Notification;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    private final SessionFactory sessionFactory;

    public NotificationResource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @POST
    public Response createNotification(Notification notification) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(notification);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(notification).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to create notification\"}")
                    .build();
        }
    }

    @GET
    public Response getAllNotifications() {
        try {
            List<Notification> notifications = getAllNotificationsFromDB();
            return Response.ok(notifications).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve notifications\"}")
                    .build();
        }
    }

    @GET
    @Path("/{notificationId}")
    public Response getNotificationById(@PathParam("notificationId") String notificationId) {
        try {
            Optional<Notification> notification = getNotificationByIdFromDB(notificationId);
            if (notification.isPresent()) {
                return Response.ok(notification.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Notification not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve notification\"}")
                    .build();
        }
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getNotificationsByCustomer(@PathParam("customerId") String customerId) {
        try {
            List<Notification> notifications = getNotificationsByCustomerFromDB(customerId);
            return Response.ok(notifications).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve customer notifications\"}")
                    .build();
        }
    }

    @GET
    @Path("/customer/{customerId}/unread")
    public Response getUnreadNotificationsByCustomer(@PathParam("customerId") String customerId) {
        try {
            List<Notification> notifications = getUnreadNotificationsByCustomerFromDB(customerId);
            return Response.ok(notifications).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve unread notifications\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{notificationId}/read")
    public Response markAsRead(@PathParam("notificationId") String notificationId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Notification notification = session.get(Notification.class, notificationId);
            if (notification != null) {
                notification.setRead(true);
                session.update(notification);
                session.getTransaction().commit();
                return Response.ok(notification).build();
            } else {
                session.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Notification not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to mark notification as read\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{notificationId}")
    public Response deleteNotification(@PathParam("notificationId") String notificationId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Notification notification = session.get(Notification.class, notificationId);
            if (notification != null) {
                session.delete(notification);
                session.getTransaction().commit();
                return Response.noContent().build();
            } else {
                session.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Notification not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to delete notification\"}")
                    .build();
        }
    }

    // Private helper methods
    private List<Notification> getAllNotificationsFromDB() {
        try (Session session = sessionFactory.openSession()) {
            Query<Notification> query = session.createQuery("FROM Notification", Notification.class);
            return query.list();
        }
    }

    private Optional<Notification> getNotificationByIdFromDB(String notificationId) {
        try (Session session = sessionFactory.openSession()) {
            Notification notification = session.get(Notification.class, notificationId);
            return Optional.ofNullable(notification);
        }
    }

    private List<Notification> getNotificationsByCustomerFromDB(String customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Notification> query = session.createQuery(
                "FROM Notification WHERE customerId = :customerId ORDER BY timestamp DESC", Notification.class);
            query.setParameter("customerId", customerId);
            return query.list();
        }
    }

    private List<Notification> getUnreadNotificationsByCustomerFromDB(String customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Notification> query = session.createQuery(
                "FROM Notification WHERE customerId = :customerId AND read = false ORDER BY timestamp DESC", Notification.class);
            query.setParameter("customerId", customerId);
            return query.list();
        }
    }
}
