package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.awt.font.GlyphMetrics;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (current_map, map1, map2, map3, saved_at, player_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, state.getCurrentMap());
            statement.setString(2, state.getMap1());
            statement.setString(3, state.getMap2());
            statement.setString(4, state.getMap3());
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            statement.setInt(6, state.getPlayerID() );
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding a new game state", e);
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET current_map = ?, map1 = ?, map2 = ?, map3 = ?, saved_at = ? " +
                    "WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, state.getCurrentMap());
            st.setString(2, state.getMap1());
            st.setString(3, state.getMap2());
            st.setString(4, state.getMap3());
            st.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            st.setInt(6, state.getPlayerID());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating a game state",e);
        }
    }

    /*@Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT current_map, map1, map2, map3, saved_at FROM game_state WHERE id LIKE ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(!rs.next()){
                return null;
            }
            //GameState state = new GameState();
            //state.setCurrentMap(rs.getString(1));
            // state.setMap1(rs.getInt(2));
            // state.setMap2(rs.getInt(3));
            // state.setMap3(rs.getInt(4));
            state.setSavedAt(rs.getDate(5));
            // state.setPlayerId(rs.getInt(6));

            return state;

        } catch (SQLException e) {
            throw new RuntimeException("Error while searching for a player", e);
        }
    }*/

    @Override
    public List<GameState> getAll(PlayerModel player, int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql =    "SELECT current_map, map1, map2, map3, saved_at " +
                            "FROM game_state " +
                            "WHERE player_id = ? ";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            List<GameState> GameStates = new ArrayList<>();
            if (!rs.isBeforeFirst()) {
                return null;
            }
            while(rs.next()){
                Timestamp savedAt = rs.getTimestamp(5);
                String map1 = rs.getString(2);
                String map2 = rs.getString(3);
                String map3 = rs.getString(4);

                GameState state = new GameState(map1, map2, map3, id);
                state.setSavedAt(savedAt);
                state.setCurrentMap(rs.getInt(1));
                GameStates.add(state);
            }
            return GameStates;

        } catch (SQLException e) {
            throw new RuntimeException("Error while reading gamestates",e);
        }
    }

}
