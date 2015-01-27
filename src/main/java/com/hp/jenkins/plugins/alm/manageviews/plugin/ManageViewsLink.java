package com.hp.jenkins.plugins.alm.manageviews.plugin;

import com.hp.jenkins.plugins.alm.manageviews.core.JsProxyTree;
import com.hp.jenkins.plugins.alm.manageviews.core.TreeToJstreeCompatibleJsonSerializer;
import com.hp.jenkins.plugins.alm.manageviews.core.ViewWrapperToJstreeCompatibleJsonSerializer;
import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers.ViewWrapper;
import com.thoughtworks.xstream.XStream;
import hudson.Extension;
import hudson.Messages;
import hudson.model.Descriptor;
import hudson.model.ManagementLink;
import hudson.model.View;
import hudson.model.ViewGroup;
import hudson.plugins.nested_view.NestedView;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

@Extension
public class ManageViewsLink extends ManagementLink {

    private JsProxyTree jsProxy;

    public ManageViewsLink() {

        jsProxy = /*
            new JsProxyTree<Integer>(
                    new NaiveHashBasedTree<Integer>(0),
                    new TreeMockJsonSerializer<Integer>()
                );
                //*/
            new JsProxyTree<ViewWrapper>(
                new ViewsTree(Jenkins.getInstance()),
                new TreeToJstreeCompatibleJsonSerializer(
                        new ViewWrapperToJstreeCompatibleJsonSerializer())
            );
    }



    @Override
    public String getIconFileName() {
        return "/plugin/views-manager/images/48x48/treeManager.png";
    }

    public String getDisplayName() {
        return "Manage Views";
    }

    @Override
    public String getUrlName() {
        return "manageViews";
    }

    public JsProxyTree getJsProxy() {
        return this.jsProxy;
    }

    @Override
    public String getDescription() {
        return "Displays an editable tree like GUI composed of jenkins View elements, allowing you to manage your views easily.";
    }



    private void renameViewsRecursively(View root) throws Descriptor.FormException {

        if (root instanceof NestedView) {
            for (View v : ((NestedView) root).getViews()) {
                v.rename(v.getViewName() + "-clone");
                renameViewsRecursively(v);
            }
        }
    }

    public static <T> T clone(T views) {

        XStream xs = new XStream2();
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        xs.toXML(views,o);
        InputStream in = new ByteArrayInputStream(o.toByteArray());
        final Object clone = xs.fromXML(in);

        return (T)clone;
    }


    @ExportedBean
    public static class ExportedView {

        View wrappedView;

        public ExportedView(View v) {
            wrappedView = v;
        }

        @Exported
        public Collection<ExportedView> getNestedViews() {
            if (wrappedView instanceof NestedView) {
                NestedView wnv = (NestedView)wrappedView;
                final Collection<View> views = wnv.getViews();
                final Collection<ExportedView> ret = new ArrayList(views.size());
                for (View v : views) {
                    ret.add(new ExportedView(v));
                }
                return ret;
            }
            else {
                return null;
            }
        }

        @Exported
        public String getViewType() {
            return wrappedView.getClass().getSimpleName();
        }

        @Exported
        public String getName() {
            return wrappedView.getDisplayName();
        }

    }


    /*
    * TODO: unite with clonejobs...
    * */

    public static void setOwner(View view, ViewGroup owner) {
        try {
            final Field ownerField = View.class.getDeclaredField("owner");
            ownerField.setAccessible(true);
            ownerField.set(view, owner);
            /*
            final Method setOwner = NestedView.class.getDeclaredMethod("setOwner", ViewGroup.class);
            setOwner.setAccessible(true);
            setOwner.invoke(view, owner);
            */
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("failed to set owner via reflection for " +
                    view.getViewName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("failed to set owner via reflection for " +
                    view.getViewName(), e);
        }
    }

    public static void AddView(NestedView container, View contained) {

        try {
            final Method addView = NestedView.class.getDeclaredMethod("addView", View.class);
            addView.setAccessible(true);
            addView.invoke(container, contained);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("failed to set add view '" + contained.getViewName() +
                    "' to '" + container.getViewName() + "' via reflection", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("failed to set add view '" + contained.getViewName() +
                    "' to '" + container.getViewName() + "' via reflection", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("failed to set add view '" + contained.getViewName() +
                    "' to '" + container.getViewName() + "' via reflection", e);
        }
    }

}

