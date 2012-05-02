package org.ovirt.engine.ui.uicommonweb.models.vms;

import org.ovirt.engine.core.common.businessentities.DisplayType;
import org.ovirt.engine.core.common.businessentities.QuotaEnforcementTypeEnum;
import org.ovirt.engine.core.common.businessentities.StoragePoolStatus;
import org.ovirt.engine.core.common.businessentities.StorageType;
import org.ovirt.engine.core.common.businessentities.VDSGroup;
import org.ovirt.engine.core.common.businessentities.VmTemplate;
import org.ovirt.engine.core.common.businessentities.storage_pool;
import org.ovirt.engine.core.compat.Event;
import org.ovirt.engine.core.compat.EventArgs;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.compat.KeyValuePairCompat;
import org.ovirt.engine.core.compat.StringHelper;
import org.ovirt.engine.ui.frontend.AsyncQuery;
import org.ovirt.engine.ui.frontend.INewAsyncCallback;
import org.ovirt.engine.ui.uicommonweb.DataProvider;
import org.ovirt.engine.ui.uicommonweb.Linq;
import org.ovirt.engine.ui.uicommonweb.dataprovider.AsyncDataProvider;
import org.ovirt.engine.ui.uicommonweb.models.EntityModel;
import org.ovirt.engine.ui.uicommonweb.models.SystemTreeItemModel;
import org.ovirt.engine.ui.uicommonweb.models.pools.PoolModel;
import org.ovirt.engine.ui.uicommonweb.validation.IValidation;
import org.ovirt.engine.ui.uicommonweb.validation.IntegerValidation;
import org.ovirt.engine.ui.uicommonweb.validation.LengthValidation;
import org.ovirt.engine.ui.uicommonweb.validation.NotEmptyValidation;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class NewPoolModelBehavior extends VmModelBehaviorBase<PoolModel>
{
    private Event poolModelBehaviorInitializedEvent = new Event("PoolModelBehaviorInitializedEvent", //$NON-NLS-1$
            NewPoolModelBehavior.class);

    public Event getPoolModelBehaviorInitializedEvent()
    {
        return poolModelBehaviorInitializedEvent;
    }

    private void setPoolModelBehaviorInitializedEvent(Event value)
    {
        poolModelBehaviorInitializedEvent = value;
    }

    @Override
    public void Initialize(SystemTreeItemModel systemTreeSelectedItem)
    {
        super.Initialize(systemTreeSelectedItem);

        getModel().getDisksAllocationModel().setIsVolumeFormatAvailable(false);

        AsyncDataProvider.GetDataCenterList(new AsyncQuery(getModel(), new INewAsyncCallback() {
            @Override
            public void OnSuccess(Object target, Object returnValue) {

                UnitVmModel model = (UnitVmModel) target;
                ArrayList<storage_pool> list = new ArrayList<storage_pool>();
                for (storage_pool a : (ArrayList<storage_pool>) returnValue) {
                    if (a.getstatus() == StoragePoolStatus.Up) {
                        list.add(a);
                    }
                }
                model.SetDataCenter(model, list);

                getPoolModelBehaviorInitializedEvent().raise(this, EventArgs.Empty);

            }
        }, getModel().getHash()));
    }

    @Override
    public void DataCenter_SelectedItemChanged()
    {
        storage_pool dataCenter = (storage_pool) getModel().getDataCenter().getSelectedItem();

        if (dataCenter == null)
            return;

        getModel().setIsHostAvailable(dataCenter.getstorage_pool_type() != StorageType.LOCALFS);

        AsyncDataProvider.GetClusterList(new AsyncQuery(new Object[] {this, getModel()}, new INewAsyncCallback() {
            @Override
            public void OnSuccess(Object target, Object returnValue) {

                Object[] array = (Object[]) target;
                NewPoolModelBehavior behavior = (NewPoolModelBehavior) array[0];
                UnitVmModel model = (UnitVmModel) array[1];
                ArrayList<VDSGroup> clusters = (ArrayList<VDSGroup>) returnValue;
                model.SetClusters(model, clusters, null);
                behavior.InitTemplate();
                behavior.InitCdImage();

            }
        }, getModel().getHash()), dataCenter.getId());

        if (dataCenter.getQuotaEnforcementType() != QuotaEnforcementTypeEnum.DISABLED) {
            getModel().getQuota().setIsAvailable(true);
        } else {
            getModel().getQuota().setIsAvailable(false);
        }
    }

    @Override
    public void Template_SelectedItemChanged()
    {
        VmTemplate template = (VmTemplate) getModel().getTemplate().getSelectedItem();

        if (template != null)
        {
            updateQuotaByCluster(template.getQuotaId());
            // Copy VM parameters from template.
            getModel().getOSType().setSelectedItem(template.getos());
            getModel().getNumOfSockets().setEntity(template.getnum_of_sockets());
            getModel().getTotalCPUCores().setEntity(template.getnum_of_cpus());
            getModel().getNumOfMonitors().setSelectedItem(template.getnum_of_monitors());
            getModel().getDomain().setSelectedItem(template.getdomain());
            getModel().getMemSize().setEntity(template.getmem_size_mb());
            getModel().getUsbPolicy().setSelectedItem(template.getusb_policy());
            getModel().setBootSequence(template.getdefault_boot_sequence());
            getModel().getIsHighlyAvailable().setEntity(template.getauto_startup());

            boolean hasCd = !StringHelper.isNullOrEmpty(template.getiso_path());

            getModel().getCdImage().setIsChangable(hasCd);
            getModel().getCdAttached().setEntity(hasCd);
            if (hasCd) {
                getModel().getCdImage().setSelectedItem(template.getiso_path());
            }

            if (!StringHelper.isNullOrEmpty(template.gettime_zone()))
            {
                // Patch! Create key-value pair with a right key.
                getModel().getTimeZone()
                        .setSelectedItem(new KeyValuePairCompat<String, String>(template.gettime_zone(), "")); //$NON-NLS-1$

                UpdateTimeZone();
            }
            else
            {
                UpdateDefaultTimeZone();
            }

            // Update domain list
            UpdateDomain();

            ArrayList<VDSGroup> clusters = (ArrayList<VDSGroup>) getModel().getCluster().getItems();
            VDSGroup selectCluster =
                    Linq.FirstOrDefault(clusters, new Linq.ClusterPredicate(template.getvds_group_id()));

            getModel().getCluster().setSelectedItem((selectCluster != null) ? selectCluster
                    : Linq.FirstOrDefault(clusters));

            // Update display protocol selected item
            EntityModel displayProtocol = null;
            boolean isFirst = true;
            for (Object item : getModel().getDisplayProtocol().getItems())
            {
                EntityModel a = (EntityModel) item;
                if (isFirst)
                {
                    displayProtocol = a;
                    isFirst = false;
                }
                DisplayType dt = (DisplayType) a.getEntity();
                if (dt == template.getdefault_display_type())
                {
                    displayProtocol = a;
                    break;
                }
            }
            getModel().getDisplayProtocol().setSelectedItem(displayProtocol);

            // By default, take kernel params from template.
            getModel().getKernel_path().setEntity(template.getkernel_url());
            getModel().getKernel_parameters().setEntity(template.getkernel_params());
            getModel().getInitrd_path().setEntity(template.getinitrd_url());

            getModel().setIsDisksAvailable(getModel().getIsNew());
            getModel().getProvisioning().setIsAvailable(false);

            if (!template.getId().equals(Guid.Empty))
            {
                getModel().getStorageDomain().setIsChangable(true);

                getModel().setIsBlankTemplate(false);
                InitDisks();
            }
            else
            {
                getModel().getStorageDomain().setIsChangable(false);

                getModel().setIsBlankTemplate(true);
                getModel().setIsDisksAvailable(false);
                getModel().setDisks(null);
            }

            getModel().getProvisioning().setEntity(false);

            InitPriority(template.getpriority());
            InitStorageDomains();
            UpdateMinAllocatedMemory();
        }
    }

    @Override
    public void Cluster_SelectedItemChanged()
    {
        UpdateDefaultHost();
        UpdateIsCustomPropertiesAvailable();
        UpdateMinAllocatedMemory();
        UpdateNumOfSockets();
        if ((VmTemplate) getModel().getTemplate().getSelectedItem() != null) {
            updateQuotaByCluster(((VmTemplate) getModel().getTemplate().getSelectedItem()).getQuotaId());
        }
    }

    @Override
    public void DefaultHost_SelectedItemChanged()
    {
        UpdateCdImage();
    }

    @Override
    public void Provisioning_SelectedItemChanged()
    {
    }

    @Override
    public void UpdateMinAllocatedMemory()
    {
        VDSGroup cluster = (VDSGroup) getModel().getCluster().getSelectedItem();
        if (cluster == null)
        {
            return;
        }

        double overCommitFactor = 100.0 / cluster.getmax_vds_memory_over_commit();
        getModel().getMinAllocatedMemory()
                .setEntity((int) ((Integer) getModel().getMemSize().getEntity() * overCommitFactor));
    }

    private void InitTemplate()
    {
        storage_pool dataCenter = (storage_pool) getModel().getDataCenter().getSelectedItem();

        AsyncDataProvider.GetTemplateListByDataCenter(new AsyncQuery(this, new INewAsyncCallback() {
            @Override
            public void OnSuccess(Object target1, Object returnValue1) {

                ArrayList<VmTemplate> loadedTemplates = (ArrayList<VmTemplate>) returnValue1;

                ArrayList<VmTemplate> templates = new ArrayList<VmTemplate>();
                for (VmTemplate template : loadedTemplates) {
                    if (!template.getId().equals(Guid.Empty)) {
                        templates.add(template);
                    }
                }
                getModel().getTemplate().setItems(templates);
                // Template.Value = templates.FirstOrDefault();
                getModel().getTemplate().setSelectedItem(Linq.FirstOrDefault(templates));
            }
        }), dataCenter.getId());

        /*
         * //Filter according to system tree selection. if (getSystemTreeSelectedItem() != null &&
         * getSystemTreeSelectedItem().getType() == SystemTreeItemType.Storage) { storage_domains storage =
         * (storage_domains)getSystemTreeSelectedItem().getEntity();
         *
         * AsyncDataProvider.GetTemplateListByDataCenter(new AsyncQuery(new Object[] { this, storage }, new
         * INewAsyncCallback() {
         *
         * @Override public void OnSuccess(Object target1, Object returnValue1) {
         *
         * Object[] array1 = (Object[])target1; NewPoolModelBehavior behavior1 = (NewPoolModelBehavior)array1[0];
         * storage_domains storage1 = (storage_domains)array1[1]; AsyncDataProvider.GetTemplateListByStorage(new
         * AsyncQuery(new Object[] { behavior1, returnValue1 }, new INewAsyncCallback() {
         *
         * @Override public void OnSuccess(Object target2, Object returnValue2) { Object[] array2 = (Object[])target2;
         * NewPoolModelBehavior behavior2 = (NewPoolModelBehavior)array2[0]; ArrayList<VmTemplate>
         * templatesByDataCenter = (ArrayList<VmTemplate>)array2[1]; ArrayList<VmTemplate>
         * templatesByStorage = (ArrayList<VmTemplate>)returnValue2; VmTemplate blankTemplate =
         * Linq.FirstOrDefault(templatesByDataCenter, new Linq.TemplatePredicate(Guid.Empty)); if (blankTemplate !=
         * null) { templatesByStorage.add(0, blankTemplate); }
         * behavior2.PostInitTemplate((ArrayList<VmTemplate>)returnValue2);
         *
         * } }), storage1.getid());
         *
         * } }, getModel().getHash()), dataCenter.getId()); } else { AsyncDataProvider.GetTemplateListByDataCenter(new
         * AsyncQuery(this, new INewAsyncCallback() {
         *
         * @Override public void OnSuccess(Object target, Object returnValue) {
         *
         * NewPoolModelBehavior behavior = (NewPoolModelBehavior)target;
         * behavior.PostInitTemplate((ArrayList<VmTemplate>)returnValue);
         *
         * } }, getModel().getHash()), dataCenter.getId()); }
         */
    }

    private void PostInitTemplate(ArrayList<VmTemplate> templates)
    {
        // If there was some template selected before, try select it again.
        VmTemplate oldTemplate = (VmTemplate) getModel().getTemplate().getSelectedItem();

        getModel().getTemplate().setItems(templates);

        getModel().getTemplate().setSelectedItem(Linq.FirstOrDefault(templates,
                oldTemplate != null ? new Linq.TemplatePredicate(oldTemplate.getId())
                        : new Linq.TemplatePredicate(Guid.Empty)));
    }

    public void InitCdImage()
    {
        UpdateCdImage();
    }

    @Override
    public void UpdateIsDisksAvailable()
    {
    }

    @Override
    public boolean Validate() {

        // Revalidate name field.
        // TODO: Make maximum characters value depend on number of desktops in pool.
        // VmOsType os = (VmOsType) getModel().getOSType().getSelectedItem();

        boolean isNew = getModel().getIsNew();
        int maxAllowedVms = DataProvider.GetMaxVmsInPool(); // TODO: Make async call to get that value.
        int assignedVms = getModel().getAssignedVms().AsConvertible().Integer();

        getModel().getNumOfDesktops().ValidateEntity(
            new IValidation[]
                {
                    new NotEmptyValidation(),
                    new LengthValidation(4),
                    new IntegerValidation(isNew ? 1 : 0, isNew ? maxAllowedVms : maxAllowedVms - assignedVms)
                });

        getModel().getPrestartedVms().ValidateEntity(
            new IValidation[]
                {
                    new NotEmptyValidation(),
                    new IntegerValidation(0, assignedVms)
                });

        getModel().setIsGeneralTabValid(getModel().getIsGeneralTabValid()
            && getModel().getName().getIsValid()
            && getModel().getNumOfDesktops().getIsValid()
            && getModel().getPrestartedVms().getIsValid());

        getModel().setIsPoolTabValid(true);

        return super.Validate()
            && getModel().getName().getIsValid()
            && getModel().getNumOfDesktops().getIsValid()
            && getModel().getPrestartedVms().getIsValid();
    }
}
