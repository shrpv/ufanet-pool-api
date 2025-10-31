package ru.ufanet.pool_api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import ru.ufanet.pool_api.exception.DatabaseException;
import ru.ufanet.pool_api.model.Client;
import ru.ufanet.pool_api.model.ClientName;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {
    private static String COLUMNS = "id, name, email, phone, created_at, updated_at";

    private final DataSource dataSource;

    @Override
    public Client save(Client client) {
        String sql = "insert into clients (name, email, phone) " +
                "values (?, ?, ?) " +
                "returning " + COLUMNS;
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPhone());
            return executeQuery(ps);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to create client", e);
        }
    }

    @Override
    public Client update(Client client) {
        String sql = "update clients " +
                "set name = ?, email = ?, phone = ?, updated_at = now() " +
                "where id = ? returning " + COLUMNS;
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPhone());
            ps.setLong(4, client.getId());
            return executeQuery(ps);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update client", e);
        }
    }

    @Override
    public List<ClientName> findAll() {
        String sql = "select id, name from clients";
        List<ClientName> clients = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ClientName result = new ClientName();
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                clients.add(result);
            }

            return clients;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch clients", e);
        }
    }

    @Override
    public Optional<Client> findById(int id) {
        String sql = "select " + COLUMNS + " from clients where id = ?";

        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch client", e);
        }
    }

    @Override
    public boolean emailExists(String email) {
        return exists("email", email);
    }

    @Override
    public boolean phoneExists(String phone) {
        return exists("phone", phone);
    }

    private boolean exists(String key, String value) {
        String sql = "select exists(select 1 from clients where " + key + " = ?)";
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, value);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed select exists " + key, e);
        }
        return false;
    }

    private Client executeQuery(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        // never executed
        return null;
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client result = new Client();
        result.setId(rs.getLong("id"));
        result.setName(rs.getString("name"));
        result.setEmail(rs.getString("email"));
        result.setPhone(rs.getString("phone"));
        result.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        result.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return result;
    }
}
