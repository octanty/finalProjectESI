package com.example.demo.sales.web;


import com.example.demo.inventory.application.dto.CatalogQueryDTO;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.sales.domain.model.PurchaseOrder;
import com.example.demo.sales.domain.model.POStatus;
import com.example.demo.inventory.domain.repository.InventoryRepository;
import com.example.demo.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

import static java.lang.Long.parseLong;

@Controller
@RequestMapping("/dashboard")
public class	DashboardController	{

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping("/catalog/form")
    public String getQueryForm(Model model)	{
        model.addAttribute("catalogQuery",	new CatalogQueryDTO());
        return	"dashboard/catalog/query-form";
    }

    @PostMapping("/catalog/query")
    public String postQueryForm(Model model, CatalogQueryDTO catalogQuery,
                               HttpServletRequest request, RedirectAttributes redirectAttributes)	{
//        model.addAttribute("catalogQuery", catalogQuery);
        System.out.print("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
        System.out.print(catalogQuery);
//        redirectAttributes.addAttribute("catalogQuery", catalogQuery);
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return	"redirect:/dashboard/catalog/q";
    }

    @GetMapping ("/catalog/q")
    public String getList(Model model, CatalogQueryDTO catalogQuery)	{
        System.out.print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        System.out.print(catalogQuery);
        List<PlantInventoryEntry> entries = inventoryRepository.findAvailablePlants(catalogQuery.getName(),catalogQuery.getRentalPeriod().getStartDate(),
                catalogQuery.getRentalPeriod().getEndDate());
//
        model.addAttribute("plants", entries);
//        BusinessPeriod b = new BusinessPeriod(catalogQuery.getRentalPeriod());

//        PurchaseOrder po = new PurchaseOrder();
//        po.setRentalPeriod(catalogQuery.getRentalPeriod());
//        model.addAttribute("po", new PurchaseOrder());
        return	"dashboard/catalog/query-result";
    }

    @PostMapping("/orders")
    public  String createOrder( PurchaseOrder po, PlantInventoryEntry p,
                               HttpServletRequest request, RedirectAttributes redirectAttributes){
        System.out.print("\n");

        System.out.print("bbbbbbbbbbbbbbb");

        System.out.print(po);
//        System.out.print(po.getRentalPeriod().getEndDate());
//        po.setRentalPeriod(r);
        po.setIssueDate(LocalDate.now());
        po.setPaymentSchedule(LocalDate.now());
        po.setStatus(POStatus.OPEN);

//        long diff = ChronoUnit.DAYS.between( po.getRentalPeriod().getEndDate(),po.getRentalPeriod().getStartDate());
//        BigDecimal total = p.getPrice().multiply(new BigDecimal(diff));
//        po.setTotal(total);
        System.out.print(po);
        purchaseOrderRepository.save(po);
        PurchaseOrder ps = po;
//        long diff = ChronoUnit.DAYS.between( ps.getRentalPeriod().getEndDate(),ps.getRentalPeriod().getStartDate());

        redirectAttributes.addAttribute("po", ps);
        return	"redirect:/dashboard/catalog/po";
    }

    @GetMapping("/catalog/po")
    public String displayPO (Model model, @RequestParam PurchaseOrder po){
        model.addAttribute("po",po);
        return "dashboard/catalog/po";
    }






}

