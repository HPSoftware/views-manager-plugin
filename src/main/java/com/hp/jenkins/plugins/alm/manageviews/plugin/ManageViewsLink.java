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

    /*
    static {
        Stapler.CONVERT_UTILS.register(new MyIntegerConverter(), MyInteger.class);
    }

    public static class MyIntegerConverter implements Converter {

        @Override
        public Object convert(Class type, Object value) {
            if (value instanceof Integer) {
                return new MyInteger((Integer)value);
            }
            if (value instanceof String) {
                return new MyInteger(Integer.parseInt((String)value));
            }
            throw new RuntimeException("can't convert type " + value.getClass() + " to " + type.getSimpleName());
        }
    }
    public static class MyInteger {
        private Integer i;

        public MyInteger(Integer i) {
            this.i = i;
        }

        public MyString toMyString() {
            return new MyString("" + i);
        }
    }
    @ExportedBean
    public static class MyString {

        private String s;

        public MyString(String s) {
            this.s  = s;
        }

        @Exported
        public String getString() {
            return this.s;
        }

        @Exported
        public String someOtherString() {
            return "bla";
        }
    }

    public class JsProxyTree {

        @JavaScriptMethod
        public JSONArray getIntegerString(MyInteger mint) {
            //return mint.toMyString();
            //return Calendar.getInstance().getTime();
            return JSONArray.fromObject("[\n" +
                    "        {\n" +
                    "            //name to display\n" +
                    "            \"data\" : \"Jenkins\",\n" +
                    "\n" +
                    "            //attributes\n" +
                    "            \"attr\" : {\n" +
                    "\n" +
                    "                //type attribute\n" +
                    "                \"rel\" : \"Jenkins\"\n" +
                    "            },\n" +
                    "\n" +
                    "            //child nodes\n" +
                    "            \"children\" : [\n" +
                    "\n" +
                    "                {   \"data\" : \"job\",\n" +
                    "                    \"attr\" : {\n" +
                    "                        \"rel\": \"Job\"\n" +
                    "                    }\n" +
                    "\n" +
                    "                },\n" +
                    "                {   \"data\" : \"list\",\n" +
                    "                    \"attr\" : {\n" +
                    "                        \"rel\": \"ListView\"\n" +
                    "                    }\n" +
                    "\n" +
                    "                },\n" +
                    "                {   \"data\" : \"nested\",\n" +
                    "                    \"attr\" : {\n" +
                    "                        \"rel\": \"NestedView\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ];");
        }

        @JavaScriptMethod
        public String getRootNode() throws IOException, Descriptor.FormException {

            final Collection<View> views = Jenkins.getInstance().getViews();

            if (views == null ||
                    (views.size() == 1 && Jenkins.getInstance().getView("All") != null)) {
                return "null";
            }

            views.remove(Jenkins.getInstance().getView("All")); //don't include all view

            Collection<View> clonedViews = ManageViewsLink.this.clone(views);

            NestedView root = new NestedView("root");
            //*get exceptions when trying to serialize if we don't do this...*
            setOwner(root,root);

            for (View v : clonedViews) {
                AddView(root, v);
                if (v instanceof NestedView) {
                    setOwner((NestedView)v,root);
                }
            }

            ManageViewsLink.this.renameViewsRecursively(root);

            ExportedView exported = new ExportedView(root);

//            Writer w = new CharArrayWriter();
//            ModelBuilder mb = new ModelBuilder();
//            final Model<ExportedView> model = mb.get(ExportedView.class);
//
//            model.writeTo(exported,
//                    new TreePruner() {
//                        @Override
//                        public TreePruner accept(Object node, Property prop) {
//                            return this;  //!= null means show everything
//                        }
//                    },
//                    Flavor.JSON.createDataWriter(exported, w));
//
//            final String jsonString = w.toString();

           return jsonString;
        }
    }
    */

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

