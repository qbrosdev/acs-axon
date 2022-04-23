package com.qbros.uithymeleaf.controllers.manager;

import com.qbros.acs.api.responses.personnel.PagedPersonnelSummary;
import com.qbros.acs.api.responses.personnel.PersonnelDetails;
import com.qbros.uithymeleaf.controllers.AbsController;
import com.qbros.uithymeleaf.models.PersonnelModel;
import com.qbros.uithymeleaf.models.paging.PagedModel;
import com.qbros.uithymeleaf.services.PersonnelService;
import com.qbros.uithymeleaf.services.mappers.PersonnelServiceMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("manager/personnel")
@RequiredArgsConstructor
@Slf4j
@SessionAttributes("personnelPagedModel")
public class PersonnelMgntCtrl extends AbsController {

    private static final String PAGE = "personnel";
    private static final String CONTENT_FAG = PAGE + "::content";
    private static final String EDIT_DIALOG_FAG = PAGE + "::editDialog";
    private static final String PERSONNEL_PAGED_MODEL = "personnelPagedModel";
    private static final String NEW_PERSONNEL_MODEL = "newPersonnel";
    private static final String EDIT_PERSONNEL_MODEL = "editPersonnel";
    private final PersonnelService personnelService;
    private final PersonnelServiceMapper mapper;

    @GetMapping("")
    public String getPage() {
        return PAGE;
    }

    @GetMapping("getById")
    public String personDetails(Model model, @RequestParam String idx) {
        return Try.of(() -> {
                    PersonnelDetails result = personnelService.getById(mapper.toPersonnelByIdQuery(idx));
                    return mapper.toPersonnelModel(result);
                })
                .onSuccess(pm -> model.addAttribute(EDIT_PERSONNEL_MODEL, pm))
                .onFailure(throwable -> addErrorMsg(model, throwable))
                .transform(voids -> EDIT_DIALOG_FAG);
    }

    @GetMapping("list")
    public String getPersonnelList(Model model,
                                   @RequestParam(required = false, defaultValue = "5") int size,
                                   @RequestParam(required = false, defaultValue = "1") int idx) {

        return handleFailures(Try.of(() -> getPersonnelList(idx, size)), model);
    }

    @PostMapping("add")
    public String addPersonnel(Model model,
                               @ModelAttribute(NEW_PERSONNEL_MODEL) PersonnelModel viewModel,
                               @ModelAttribute(PERSONNEL_PAGED_MODEL) PagedModel<PersonnelModel> personnelPagedModel) {
        return handleFailures(
                Try.of(() -> {
                    personnelService.executeSynchronousCommand(mapper.toAddPerCommand(viewModel));
                    return gotoFirstPage(personnelPagedModel);
                }), model);
    }

    @PostMapping("edit")
    public String editPersonnel(Model model,
                                @ModelAttribute(EDIT_PERSONNEL_MODEL) PersonnelModel viewModel,
                                @ModelAttribute(PERSONNEL_PAGED_MODEL) PagedModel<PersonnelModel> personnelPagedModel) {
        return handleFailures(
                Try.of(() -> {
                    personnelService.executeSynchronousCommand(mapper.toEditPersonnelCommand(viewModel));
                    return gotoFirstPage(personnelPagedModel);
                }), model);
    }

    @PostMapping("delete")
    public String deletePersonnel(Model model,
                                  @ModelAttribute("personnel_id") String perId,
                                  @ModelAttribute(PERSONNEL_PAGED_MODEL) PagedModel<PersonnelModel> personnelPagedModel) {
        return handleFailures(
                Try.of(() -> {
                    personnelService.executeSynchronousCommand(mapper.toDeleteCommand(perId));
                    return gotoFirstPage(personnelPagedModel);
                }), model);
    }

    @ModelAttribute(PERSONNEL_PAGED_MODEL)
    protected PagedModel<PersonnelModel> getPersonnelPagedModel() {
        return PagedModel.emptyPage(5);
    }

    @ModelAttribute(NEW_PERSONNEL_MODEL)
    protected PersonnelModel newPersonnel() {
        return new PersonnelModel();
    }

    @ModelAttribute(EDIT_PERSONNEL_MODEL)
    protected PersonnelModel editPersonnel() {
        return new PersonnelModel();
    }

    private PagedModel<PersonnelModel> gotoFirstPage(PagedModel<PersonnelModel> personnelPagedModel) {
        return getPersonnelList(1, personnelPagedModel.getPagingNav().getPageSize());
    }

    private PagedModel<PersonnelModel> getPersonnelList(int idx, int size) {
        PagedPersonnelSummary result = personnelService.get(mapper.toPersonnelSummaryQuery(idx, size));
        return mapper.toPagedPersonnelModel(result);
    }

    private String handleFailures(Try<Object> tRy, Model model) {
        return tRy
                .onSuccess(pm -> model.addAttribute(PERSONNEL_PAGED_MODEL, pm))
                .onFailure(throwable -> addErrorMsg(model, throwable))
                .transform(voids -> CONTENT_FAG);
    }
}
