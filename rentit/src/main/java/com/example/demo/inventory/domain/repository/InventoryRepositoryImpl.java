package com.example.demo.inventory.domain.repository;

import com.example.demo.inventory.domain.model.PlantInventoryItem;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.utils.Pair;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InventoryRepositoryImpl implements CustomInventoryRepository {

    @Autowired
    EntityManager em;

    public List<PlantInventoryEntry> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select p.plantInfo from PlantInventoryItem p where p.plantInfo.name like concat('%', ?1, '%') and p not in" +
                "(select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)",
            PlantInventoryEntry.class)
            .setParameter(1, name)
            .setParameter(2, startDate)
            .setParameter(3, endDate)
            .getResultList();
    }

    public List<Pair<String, BigDecimal>> query1() {
        String qs = "select new com.example.demo.utils.Pair(prmt.reservation.rental.plant.name, sum(prmt.reservation.rental.total - prmt.price)) from PlantReservation pr join pr.maintPlan.tasks prmt where DATEDIFF('month', pr.rental.issueDate, CURDATE()) <= 12 group by prmt.reservation.rental.plant.name";

        Query query = em.createQuery(qs, Pair.class);
        return query.getResultList();
    }

    public List<Pair<String, Long>> query2(LocalDate startDate, LocalDate endDate) {
        return em.createQuery(
            "select new com.example.demo.utils.Pair(pie.name, sum(DATEDIFF(day, ?1, ?2) - (DATEDIFF(day, pr.schedule.startDate, pr.schedule.endDate) + DATEDIFF(day, mt.taskPeriod.startDate, mt.taskPeriod.endDate)))) from PlantReservation pr " +
                "INNER JOIN PlantInventoryEntry pie ON pr.plant.id=pie.id " +
                "INNER JOIN MaintenanceTask mt ON pr.id = mt.reservation.id " +
                "WHERE pr.schedule.startDate <= ?2 AND pr.schedule.endDate >= ?1 " +
                "AND mt.taskPeriod.startDate <= ?2 AND mt.taskPeriod.endDate >= ?1 " +
                "GROUP BY pie.name")
            .setParameter(1, startDate)
            .setParameter(2, endDate)
            .getResultList();
    }


    public List<Pair<String, Long>> query3(String name, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select new com.example.demo.utils.Pair(p.plantInfo.name, count(p.id)) from PlantInventoryItem p where p.plantInfo.name like concat('%', ?1, '%') and p not in" +
            "(select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)"
            + "and(p.equipmentCondition ='SERVICEABLE'" +
            "or DATEDIFF(week,  CURDATE(), ?2) >= 3)  and p in (select m.reservation.plant from MaintenanceTask m where DATEDIFF(week, m.taskPeriod.endDate, ?2)  >=1) and p.equipmentCondition <>'UNSERVICEABLECONDEMNED'" +
            "GROUP BY p.plantInfo.name")
            .setParameter(1, name)
            .setParameter(2, startDate)
            .setParameter(3, endDate)
            .getResultList();
    }

    public List<String> query4() {
        return em.createNativeQuery("SELECT serial_number FROM " +
            "(SELECT plant_inv_item.serial_number, plant_inv_item.id, plant_inv_entry.price," +
            " maintenance_task.start_date,maintenance_task.end_date," +
            " COUNT(maintenance_task.id) AS NumberOfTasks FROM maintenance_task\n" +
            "INNER JOIN maintenance_plan ON maintenance_plan.id = maintenance_task.plan_id\n" +
            "INNER JOIN plant_inv_item ON plant_inv_item.id =maintenance_plan.plant_id\n" +
            "INNER JOIN plant_inv_entry ON plant_inv_entry.id =plant_inv_item.plant_info_id\n" +
            "WHERE maintenance_task.start_date > '2018-12-31' AND maintenance_task.end_date < '2020-01-01'\n" +
            "GROUP BY plant_inv_item.id \n" +
            "ORDER BY COUNT(maintenance_task.id) DESC, plant_inv_entry.price DESC\n" +
            "LIMIT 3)").getResultList();
    }

    //@Override
    public List<PlantInventoryItem> findAvailableItems(String name, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select p from PlantInventoryItem p where LOWER(p.plantInfo.name) like concat('%', LOWER(?1), '%') and p not in " +
                "(select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)",
            PlantInventoryItem.class)
            .setParameter(1, name)
            .setParameter(2, startDate)
            .setParameter(3, endDate)
            .getResultList();
    }

//    @Override
//    public List<PlantInventoryItem> findAvailableItems(PlantInventoryEntry entry, LocalDate startDate, LocalDate endDate) {
//        return em.createQuery("select i from PlantInventoryItem i where i.plantInfo = ?1 and i not in " +
//                        "(select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)"
//                , PlantInventoryItem.class)
//                .setParameter(1, entry)
//                .setParameter(2, startDate)
//                .setParameter(3, endDate)
//                .getResultList();
//    }

    @Override
    public Boolean isPlantInventoryItemExisting(Long piiId) {
        return null;
    }

    @Override
    public Boolean isPlantInventoryEntryExisting(Long pieId) {
        return null;
    }
}
