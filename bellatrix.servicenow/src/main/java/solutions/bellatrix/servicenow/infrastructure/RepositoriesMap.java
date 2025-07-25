package solutions.bellatrix.servicenow.infrastructure;

import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.TableApiRepository;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class RepositoriesMap {
    public static Map<Class<? extends ApiEntity>, Class<? extends TableApiRepository<?, ?>>> realEstateRepos() {
        var map = new HashMap<Class<? extends ApiEntity>, Class<? extends TableApiRepository<?, ?>>>();
//       The following Code Will be used when need to use MassiveDataDeleter
//        map.put(RemLeaseContract.class, RemLeaseContractRepository.class);
//        map.put(ReLeasedAsset.class, ReLeasedAssetRepository.class);
//        map.put(RemSpace.class, RemSpaceRepository.class);
//        map.put(EamBuilding.class, EamBuildingRepository.class);
//        map.put(EamFloor.class, EamFloorRepository.class);
//        map.put(ReOwnedAsset.class, ReOwnedAssetRepository.class);
//        map.put(NpsReminder.class, NpsReminderRepository.class);
//        map.put(EamCompany.class, EamCompanyRepository.class);
//        map.put(ReAccounting.class, ReAccountingRepository.class);
//        map.put(ReAccountingCalculations.class, ReAccountingCalculationsRepository.class);
//        map.put(RemContact.class, RemContactRepository.class);
//        map.put(RePaymentLine.class, RePaymentLineRepository.class);
//        map.put(RePaymentSchedule.class, RePaymentScheduleRepository.class);
//        map.put(ReActivities.class, ReActivitiesRepository.class);
//        map.put(ReRecurringPayment.class, ReRecurringPaymentRepository.class);
//        map.put(ReCriticalDate.class, ReCriticalDateRepository.class);
//
//        map.put(ReEscalation.class, ReEscalationRepository.class);
//        map.put(ReGlobalFilter.class, ReGlobalFilterRepository.class);
//        map.put(NpsFolder.class, NpsFolderRepository.class);
//        map.put(PortfolioProperty.class, PortfolioPropertyRepository.class);
//        map.put(EamRegion.class, EamRegionRepository.class);
//        map.put(EamZone.class, EamZoneRepository.class);
//        map.put(EamELocation.class, EamELocationRepository.class);
//        map.put(PortfolioInitiative.class, PortfolioInitiativeRepository.class);
//        map.put(PortfolioAction.class, PortfolioActionRepository.class);
//        map.put(TmRequirement.class, TmRequirementRepository.class);
//        map.put(TmTransaction.class, TmTransactionRepository.class);
//        map.put(TmTransactionTask.class, TmTransactionTaskRepository.class);
//        map.put(ReReminder.class, ReReminderRepository.class);
//        map.put(EamCampus.class, EamCampusRepository.class);
//        map.put(NpsCriticalDate.class, NpsCriticalDateRepository.class);
//        map.put(SysChoice.class, SysChoiceRepository.class);

        return map;
    }

    public static Map<Class<? extends ApiEntity>, Class<? extends TableApiRepository<?, ?>>> otSecurityRepos() {
        var map = new HashMap<Class<? extends ApiEntity>, Class<? extends TableApiRepository<?, ?>>>();
//        map.put(SysUser.class, SysUserRepository.class);

        return map;
    }

    public static Map<Class<? extends ApiEntity>, Class<? extends TableApiRepository<?, ?>>> mmRepos() {
        var map = new HashMap<Class<? extends ApiEntity>, Class<? extends TableApiRepository<?, ?>>>();
//        map.put(FacilitiesPurchaseOrderLineItem.class, FacilitiesPurchaseOrderLineItemRepository.class);
//        map.put(EamClinicalDevice.class, EamClinicalDeviceRepository.class);
//        map.put(EamClinicalWorkOrder.class, EamMmClinicalMmWorkOrderRepository.class);
//        map.put(EamFacilitiesWorkOrder.class, EamMmFacilitiesMmWorkOrderRepository.class);
//        map.put(EamFacilitiesParts.class, EamFacilitiesPartsRepository.class);
//        map.put(EamStockrooms.class, EamStockroomsRepository.class);
//        map.put(EamWorkOrderItemizeCost.class, EamWorkOrderItemizeCostRepository.class);
//        map.put(WmAisle.class, WmAisleRepository.class);
//        map.put(WmRack.class, WmRackRepository.class);
//        map.put(WmShelf.class, WmShelfRepository.class);
//        map.put(WmBin.class, WmBinRepository.class);
//        map.put(SysUser.class, SysUserRepository.class);
//        map.put(SysUserGroup.class, SysUserGroupRepository.class);
//        map.put(SysUserGroupMember.class, SysUserGroupMemberRepository.class);
//        map.put(EamClinicalWorkOrderTypes.class, structureModels.eam.repositories.EamClinicalWorkOrderTypesRepository.class);
//        map.put(EamFacilitiesWorkOrderTypes.class, structureModels.eam.repositories.EamFacilitiesWorkOrderTypesRepository.class);
//        map.put(EamFacilitiesTransferOrder.class, EamFacilitiesTransferOrderRepository.class);
//        map.put(ClinicalPurchaseOrderLineItem.class, ClinicalPurchaseOrderLineItemRepository.class);
//        map.put(EamClinicalTransferOrderLineItems.class, EamClinicalTransferOrderLineItemsRepository.class);
//        map.put(EamClinicalTransferOrder.class, EamClinicalTransferOrderRepository.class);
//        map.put(EamClinicalReceivingSlip.class, EamClinicalReceivingSlipRepository.class);
//        map.put(EamClinicalReceivingSlipLineItem.class, EamClinicalReceivingSlipLineItemRepository.class);
//        map.put(WmPickTicket.class, WmPickTicketRepository.class);
//        map.put(EamClinicalParts.class, EamClinicalPartsRepository.class);
//        map.put(EamChecklist.class, EamChecklistRepository.class);
//        map.put(EamChecklistQuestions.class, EamChecklistQuestionsRepository.class);
//        map.put(EamChecklistRules.class, ClinicalChecklistRulesRepository.class);
//        map.put(EamChecklistRules.class, FacilitiesChecklistRulesRepository.class);

        return map;
    }
}