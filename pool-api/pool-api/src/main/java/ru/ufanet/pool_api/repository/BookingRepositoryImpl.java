package ru.ufanet.pool_api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import ru.ufanet.pool_api.exception.DatabaseException;
import ru.ufanet.pool_api.model.Booking;
import ru.ufanet.pool_api.model.BookingDetails;
import ru.ufanet.pool_api.model.Client;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {
    private final DataSource dataSource;

    @Override
    public long save(Booking booking) {
        String sql = """
                insert into bookings (client_id, start_time)
                values (?, ?)
                returning id
                """;

        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, booking.getClientId());
            ps.setTimestamp(2, Timestamp.valueOf(booking.getTime()));

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create booking", e);
        }
        return 0;
    }

    @Override
    public Map<LocalDateTime, Integer> findReservedTimeSlots(LocalDate date) {
        String sql = """
                select start_time, count(*) as count
                from bookings
                where start_time >= ? and start_time < ? and deleted_at is null
                group by start_time
                order by start_time
                """;
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Map<LocalDateTime, Integer> result = new HashMap<>();
            ps.setTimestamp(1, Timestamp.valueOf(date.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(date.plusDays(1).atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime time = rs.getTimestamp("start_time").toLocalDateTime();
                    int count = rs.getInt("count");
                    result.put(time, count);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch reserved time slots", e);
        }
    }

    @Override
    public List<BookingDetails> findByDate(LocalDate date) {
        String sql = """
                select start_time, client_id, name, email, phone
                from bookings b
                join clients c on c.id = b.client_id
                where start_time >= ? and start_time < ? and deleted_at is null
                order by start_time
                """;
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Map<LocalDateTime, List<Client>> timeToClientsMap = new HashMap<>();
            ps.setTimestamp(1, Timestamp.valueOf(date.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(date.plusDays(1).atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime time = rs.getTimestamp("start_time").toLocalDateTime();
                    Client client = new Client();
                    client.setId(rs.getLong("client_id"));
                    client.setName(rs.getString("name"));
                    client.setEmail(rs.getString("email"));
                    client.setPhone(rs.getString("phone"));

                    timeToClientsMap.computeIfAbsent(time, t -> new ArrayList<>())
                            .add(client);
                }
            }

            return timeToClientsMap.entrySet().stream()
                    .collect(ArrayList::new, (list, entry) -> list.add(
                            new BookingDetails(entry.getValue(), entry.getValue().size(), entry.getKey())
                    ), ArrayList::addAll);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch bookings", e);
        }
    }

    @Override
    public void delete(long clientId, LocalDateTime time, String reason) {
        String sql = "update bookings " +
                "set deleted_at = now(), reason = ? " +
                "where client_id = ? and start_time = ? and deleted_at is null";
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setLong(2, clientId);
            ps.setTimestamp(3, Timestamp.valueOf(time));
            ps.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update client", e);
        }
    }

    @Override
    public void lock(int key) {
        String sql = "select pg_advisory_xact_lock(" + key + ")";
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            //
        } catch (SQLException e) {
            throw new DatabaseException("Failed to lock", e);
        }
    }

    @Override
    public int count(LocalDateTime dateTime) {
        String sql = """
                select count(*) as count
                from bookings
                where start_time = ? and deleted_at is null
                """;
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(dateTime));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed count", e);
        }
        return 0;
    }

    @Override
    public boolean exists(Booking booking) {
        String sql = "select exists(select 1 from bookings where client_id = ? and start_time = ?)";
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, booking.getClientId());
            ps.setTimestamp(2, Timestamp.valueOf(booking.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed select exists booking", e);
        }
        return false;
    }
}
