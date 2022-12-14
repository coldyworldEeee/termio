mod imp;

use gtk::{
    glib::{self, Object},
    subclass::prelude::*,
};

use crate::{MenuItem, MenuSection};

glib::wrapper! {
    pub struct TestMenu(ObjectSubclass<imp::TestMenu>)
        @extends gtk::Widget,
        @implements gtk::Accessible, gtk::Buildable, gtk::ConstraintTarget;
}

impl TestMenu {
    pub fn new() -> Self {
        Object::new(&[])
    }

    pub fn append_item(&self, item: MenuItem) {
        self.imp().append_item(item);
    }

    pub fn append_section(&self, section: MenuSection) {
        self.imp().append_section(section);
    }
}
