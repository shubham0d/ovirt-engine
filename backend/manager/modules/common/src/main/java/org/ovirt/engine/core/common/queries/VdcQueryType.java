package org.ovirt.engine.core.common.queries;

import java.io.Serializable;

@SuppressWarnings("unused")
public enum VdcQueryType implements Serializable {
    // VM queries
    IsVmWithSameNameExist,
    GetImportCandidates,
    GetImportCandidatesInfo,
    GetAllImportCandidatesInfo,
    GetCandidateInfo,
    GetVmByVmId,
    GetVmsRunningOnVDS,
    GetVmsRunningOnVDSCount,
    GetTopSizeVmsFromStorageDomain,
    GetVmCustomProperties,

    // Vds queries
    IsVdsWithSameNameExist,
    IsVdsWithSameHostExist,
    IsVdsWithSameIpExists,
    GetVdsByVdsId,
    GetVdsByHost,
    GetVdsByName,
    GetVdsByType,
    GetVdsFenceStatus,
    GetNewVdsFenceStatus,
    CanFenceVds,
    GetAgentFenceOptions,
    GetAgentFenceOptions2,
    GetAllChildVlanInterfaces,
    GetAllSiblingVlanInterfaces,
    GetVlanParanet,
    GetVdsHooksById,
    GetVdsHooksById2,

    // Vds Networks
    GetVdsInterfacesByVdsId,
    GetVdsFreeBondsByVdsId,
    GetAllNetworks,
    GetAllNetworksByClusterId(VdcQueryAuthType.User),
    GetNetworkDisplayByClusterId,
    GetNonOperationalVds,

    // Vm Network
    GetVmInterfacesByVmId(VdcQueryAuthType.User),

    // Template Network
    GetTemplateInterfacesByTemplateId,

    // VdsGroups
    GetVdsCertificateSubjectByVdsId,
    GetAllVdsGroups,
    GetVdsGroupByVdsGroupId,
    GetVdsGroupById,
    GetVdsGroupByName,
    IsVdsGroupWithSameNameExist,
    GetVdsGroupsByStoragePoolId,

    // Certificate
    GetCACertificate,

    // VM Templates queries
    IsVmTemlateWithSameNameExist,
    GetVmTemplate,
    GetAllVmTemplates,
    GetVmsByVmTemplateGuid,
    GetVmTemplatesDisks,
    GetVmTemplatesByStoragePoolId,
    GetSystemPermissions,

    // Images queries
    GetAllVmSnapshotsByDrive(VdcQueryAuthType.User),
    GetAllIsoImagesList,
    GetAllFloppyImagesList,
    GetAllDisksByVmId(VdcQueryAuthType.User),
    GetImageByImageId,

    // Users queries
    GetUserVmsByUserIdAndGroups,
    GetTimeLeasedUsersByVmPoolId,
    GetDbUserByUserId,
    GetUsersByVmid,
    GetVmsByUserid,
    GetUserMessage,
    GetUserBySessionId,

    // AdGroups queries
    GetAllAdGroups,
    GetAdGroupsAttachedToTimeLeasedVmPool,
    GetVmPoolsAttachedToAdGroup,
    GetAdGroupById,

    // VM pools queries
    GetVmPoolById,
    GetVmPoolsMapByVmPoolId,
    GetAllVmPools,
    HasFreeVmsInPool,
    GetAllVmPoolsAttachedToUser,
    IsVmPoolWithSameNameExists,

    // Tags queries
    GetAllTags,
    GetAllNotReadonlyTags,
    GetRootTag,
    GetTagByTagId,
    GetTagByTagName,
    GetTagsByUserGroupId,
    GetTagsByUserId,
    GetTagsByVmId,
    GetTagsByVdsId,
    GetTagUserMapByTagName,
    GetTagUserGroupMapByTagName,
    GetTagVmMapByTagName,
    GetTagVdsMapByTagName,
    GetTagIdsAndChildrenIdsByRegExp,
    GetTagIdAndChildrenIds,

    // System
    GetSystemStatistics,
    GetStorageStatistics,

    // Bookmarks
    GetBookmarkById,
    GetBookmarkByName,
    GetAllBookmarks,

    // FieldsUpdating
    CanUpdateFieldGeneric,

    // Configuration values
    GetConfigurationValue,
    GetTimeZones,
    GetDefualtTimeZone,
    GetDiskConfigurationList,
    GetAvailableClusterVersions,
    GetAvailableStoragePoolVersions,
    GetAvailableClusterVersionsByStoragePool,

    // AuditLog
    GetVdsMessages,
    GetVmsMessages,
    GetUserMessages,
    GetEventMessages,
    GetTemplateMessages,

    // Search queries
    Search,
    RegisterSearch,
    UnregisterSearch,

    // Public services
    GetDomainList,
    IsLicenseValid,
    IsLicenseSupported,
    RegisterVds,
    CheckDBConnection,

    // License queries
    GetLicenseProperties,
    GetLicenseProductType,
    GetResourceUsage,
    GetPowerClient,
    AddPowerClient,
    GetDedicatedVm,
    GetMACAddress,
    GetAllServerCpuList,
    GetAvailableClustersByServerCpu,

    // Multi Level Administration queries
    GetAllRoles,
    GetRolesByAdElement,
    GetRolesByAdElementIdAndNullTag,
    GetRoleById,
    GetRoleByName,
    GetPermissionById,
    GetPermissionByRoleId,
    GetPermissionsByAdElement,
    GetRolesByAdElementId,
    GetPermissionsByAdElementId,
    GetRoleActionGroupsByRoleId,
    IsUserPowerUserOrAbove,
    GetRolesForDelegationByUser,
    GetPermissionsForObject,
    GetDataCentersWithPermittedActionOnClusters,
    GetClustersWithPermittedAction,
    GetVmTemplatesWithPermittedAction,

    // Storage
    IsStoragePoolWithSameNameExist,
    GetStorageDomainById,
    GetStorageServerConnectionById,
    GetStoragePoolById(VdcQueryAuthType.User),
    GetStorageDomainsByConnection,
    GetStorageDomainsByStoragePoolId(VdcQueryAuthType.User),
    GetStorageServerConnections,
    GetVgList,
    GetVGInfo,
    GetDeviceList,
    DiscoverSendTargets,
    GetStorageSessionsList,
    GetStorageDomainsByVmTemplateId,
    GetVmsFromExportDomain("org.ovirt.engine.core.bll.storage"),
    GetTemplatesFromExportDomain,
    GetVmTemplatesFromStorageDomain,
    GetAllIdsFromExportDomain,
    GetExistingStorageDomainList,
    GetStorageDomainByIdAndStoragePoolId,
    GetStoragePoolsByStorageDomainId,
    GetStorageDomainListById,
    GetLunsByVgId,

    // Event Notification
    GetEventNotificationMethods,
    GetEventNotificationMethodByType,
    GetNotificationEventMap,
    GetAllEventSubscribers,
    GetEventSubscribersBySubscriberId,
    GetEventSubscribersBySubscriberIdGrouped,

    // Query registration
    RegisterQuery,
    UnregisterQuery,

    // oVirt
    GetoVirtISOs,

    // Async Tasks
    GetTasksStatusesByTasksIDs,

    // Quota
    GetQuotaByStoragePoolId,
    GetQuotaByQuotaId,
    GetQuotaVdsGroupByQuotaId,
    GetQuotaStorageByQuotaId,
    GetDisksForQuotaId,

    // Jobs
    GetJobByJobId,
    GetJobsByCorrelationId,
    GetJobsByOffset,

    // Default type instead of having to null check
    Unknown(VdcQueryAuthType.User);

    /**
     * What kind of authorization the query requires. Although this is essentially a <code>boolean</code>, it's
     * implemented as an enum for future extendability.
     */
    public static enum VdcQueryAuthType {
        Admin,
        User
    }

    private static final String DEFAULT_PACKAGE_NAME = "org.ovirt.engine.core.bll";

    private String packageName;

    private VdcQueryAuthType authType;

    private VdcQueryType() {
        packageName = DEFAULT_PACKAGE_NAME;
        authType = VdcQueryAuthType.Admin;
    }

    private VdcQueryType(String packageName) {
        this.packageName = packageName;
        authType = VdcQueryAuthType.Admin;
    }

    private VdcQueryType(VdcQueryAuthType authType) {
        packageName = DEFAULT_PACKAGE_NAME;
        this.authType = authType;
    }

    public int getValue() {
        return this.ordinal();
    }

    public static VdcQueryType forValue(int value) {
        return values()[value];
    }

    public String getPackageName() {
        return packageName;
    }

    public VdcQueryAuthType getAuthType() {
        return authType;
    }

    public boolean isAdmin() {
        return authType == VdcQueryAuthType.Admin;
    }
}
