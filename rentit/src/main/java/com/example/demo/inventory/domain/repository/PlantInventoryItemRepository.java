package com.example.demo.inventory.domain.repository;

import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.inventory.domain.model.PlantInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantInventoryItemRepository extends JpaRepository<PlantInventoryItem, Long> {
    PlantInventoryItem getOneByPlantInfo(PlantInventoryEntry entry);

    public PlantInventoryItem findOneByPlantInfo(PlantInventoryEntry entry);

    @Query(value = "SELECT serial_number FROM " +
        "(SELECT plant_inv_item.serial_number, plant_inv_item.id, plant_inv_entry.price," +
        " maintenance_task.start_date,maintenance_task.end_date," +
        "  COUNT(maintenance_task.id) AS NumberOfTasks FROM maintenance_task\n" +
        "INNER JOIN maintenance_plan ON maintenance_plan.id = maintenance_task.plan_id\n" +
        "INNER JOIN plant_inv_item ON plant_inv_item.id =maintenance_plan.plant_id\n" +
        "INNER JOIN plant_inv_entry ON plant_inv_entry.id =plant_inv_item.plant_info_id\n" +
        "WHERE maintenance_task.start_date > '2016-01-01' AND maintenance_task.end_date < '2020-01-01'\n" +
        "GROUP BY plant_inv_item.id \n" +
        "ORDER BY COUNT(maintenance_task.id) DESC, plant_inv_entry.price DESC\n" +
        "LIMIT 1);"
        , nativeQuery = true)
    List<String> query4();


    @Query(value = "SELECT serial_number FROM " +
        "(SELECT plant_inv_item.serial_number, plant_inv_item.id, plant_inv_entry.price," +
        " maintenance_task.start_date,maintenance_task.end_date," +
        "  COUNT(maintenance_task.id) AS NumberOfTasks FROM maintenance_task\n" +
        "INNER JOIN maintenance_plan ON maintenance_plan.id = maintenance_task.plan_id\n" +
        "INNER JOIN plant_inv_item ON plant_inv_item.id =maintenance_plan.plant_id\n" +
        "INNER JOIN plant_inv_entry ON plant_inv_entry.id =plant_inv_item.plant_info_id\n" +
        "WHERE maintenance_task.start_date > '2016-01-01' AND maintenance_task.end_date < '2020-01-01'\n" +
        "GROUP BY plant_inv_item.id \n" +
        "ORDER BY COUNT(maintenance_task.id) DESC, plant_inv_entry.price DESC\n" +
        "LIMIT 1);"
        , nativeQuery = true)
    List<String> query3();


    @Query(value = "SELECT a.serial_number FROM(\n" +
        "            SELECT serial_number, price,\n" +
        "            SUM(COUNT(a.id)) FROM ((SELECT  a.plan_id,\n" +
        "            COUNT(a.id) AS NumberOfTasks FROM (SELECT * FROM  maintenance_task\n" +
        "            WHERE start_date > '2016-01-01' AND end_date < '2020-01-01') a \n" +
        "            GROUP BY plan_id)\n" +
        "            INNER JOIN plant_inv_item ON plant_inv_item.id =a.plant_id)\n" +
        "            GROUP BY plant_inv_item.id \n" +
        "            INNER JOIN plant_inv_entry ON plant_inv_entry.id =plant_inv_item.plant_info_id)\n"
//            "            ORDER BY SUM(maintenance_task.id) DESC, plant_inv_entry.price DESC\n" +
//            "            LIMIT 1) a;"
        , nativeQuery = true)
    List<String> query5();

    @Query(value = "SELECT plant_inv_item.serial_number FROM plant_inv_item "
//            "            ORDER BY SUM(maintenance_task.id) DESC, plant_inv_entry.price DESC\n" +
//            "            LIMIT 1) a;"
        , nativeQuery = true)
    List<String> query6();
}
