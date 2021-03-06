package org.ovirt.engine.ui.common.uicommon;

import org.ovirt.engine.core.common.queries.ConsoleOptionsParams;
import org.ovirt.engine.core.common.queries.VdcQueryReturnValue;
import org.ovirt.engine.core.common.queries.VdcQueryType;
import org.ovirt.engine.ui.frontend.AsyncQuery;
import org.ovirt.engine.ui.frontend.Frontend;
import org.ovirt.engine.ui.uicommonweb.models.vms.ConsoleModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.ISpiceNative;

public class SpiceNativeImpl extends AbstractSpice implements ISpiceNative {

    @Override
    public void invokeClient() {
        // todo avoid code duplication with vnc
        AsyncQuery<VdcQueryReturnValue> callback = new AsyncQuery<>(returnValue ->
                ConsoleModel.makeConsoleConfigRequest("console.vv", //$NON-NLS-1$
                "application/x-virt-viewer; charset=UTF-8", //$NON-NLS-1$
                returnValue.getReturnValue())
        );

        Frontend.getInstance().runQuery(
                VdcQueryType.GetConsoleDescriptorFile,
                new ConsoleOptionsParams(getOptions()), callback);
    }
}
