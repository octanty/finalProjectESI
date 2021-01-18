package com.buildit.common.application.dto;// package com.buildit.common.application.dto;
//
//
// import com.example.demo.inventory.domain.model.EquipmentCondition;
// import com.example.demo.inventory.domain.model.PlantReservation;
// import com.example.demo.inventory.domain.repository.PlantReservationRepo;
// import com.example.demo.maintenance.application.dto.MaintenanceTaskDTO;
// import com.example.demo.maintenance.domain.model.TypeOfWork;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.validation.Errors;
// import org.springframework.validation.Validator;
//
// @Service
// public class ItemConditionValidator implements Validator {
//
//     PlantReservationRepo plantReservationRepo;
//     @Autowired
//     public ItemConditionValidator(PlantReservationRepo plantReservationRepo){
//         this.plantReservationRepo = plantReservationRepo;
//     }
//
//     @Override
//     public boolean supports(Class<?> aClass) {
//         return MaintenanceTaskDTO.class.equals(aClass);
//     }
//
//     @Override
//     public void validate(Object o, Errors errors) {
//
// //        List <Object> list = (List<Object>) o;
// //        MaintenanceTaskDTO task = (MaintenanceTaskDTO) list.get(0);
// //        PlantReservation plantReservation = (PlantReservation) list.get(1);
//         MaintenanceTaskDTO task = (MaintenanceTaskDTO) o;
//         PlantReservation plantReservation = plantReservationRepo.findById(task.getReservation().get_id()).orElse(null);
//
//         // System.out.print("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
//         // System.out.print(plantReservation.getPlant().getEquipmentCondition());
//         if( task.getTypeOfWork() == TypeOfWork.PREVENTIVE && plantReservation.getPlant().getEquipmentCondition() != EquipmentCondition.SERVICEABLE)
//             errors.rejectValue("typeOfWork", "condition cannot be anything else from SERVICEABLE for PREVENTIVE type of work");
//
//         if( task.getTypeOfWork() == TypeOfWork.CORRECTIVE &&
//                 (plantReservation.getPlant().getEquipmentCondition() != EquipmentCondition.UNSERVICEABLEREPAIRABLE &&
//                         plantReservation.getPlant().getEquipmentCondition() != EquipmentCondition.UNSERVICEABLEINCOMPLETE) )
//             errors.rejectValue("typeOfWork",
//                     "condition cannot be something else from UNSERVICEABLEREPAIRABLE or" +
//                             "  UNSERVICEABLEINCOMPLETE for CORRECTIVE type of work");
//
//
//         if( task.getTypeOfWork() == TypeOfWork.OPERATIVE && plantReservation.getPlant().getEquipmentCondition() == EquipmentCondition.UNSERVICEABLECONDEMNED)
//             errors.rejectValue("typeOfWork", "condition cannot be UNSERVICEABLECONDEMNED for OPERATIVE type of work");
//     }
// }
