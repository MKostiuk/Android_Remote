package com.components.createch.androidremote;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

import java.beans.PropertyChangeSupport;

/**
 * Created by IDA on 22/02/2017.
 */

@UpnpService(
        serviceId = @UpnpServiceId("RemoteController"),
        serviceType = @UpnpServiceType(value = "RemoteController", version = 1)
)
public class RemoteController {


        private final PropertyChangeSupport propertyChangeSupport;

        public RemoteController() {
            this.propertyChangeSupport = new PropertyChangeSupport(this);
        }

        public PropertyChangeSupport getPropertyChangeSupport() {
            return propertyChangeSupport;
        }


        /**
         * Variable D'Etat, non �venemenc�e
         * Permet d'envoyer le message de l'�tat dans lequel la lampe doit �tre
         */
        @UpnpStateVariable(defaultValue = "AUCUN", sendEvents = false)
        private String target = State.AUCUN.toString();

        /**
         * Variable d'etat �venemmenc�e
         * Permet de v�rifier si la lampe est bien dans le bon �tat.
         */
        @UpnpStateVariable(defaultValue = "AUCUN")
        private String status = State.AUCUN.toString();

        /**
         * Variable qui me permet d'emmettre des �venements UPnP et JavaBean
         */


        /**
         * Permet d'envoyer un message de changement d'etat de la lampe
         *
         * @param newTargetValue
         */
        @UpnpAction
        public void setTarget(@UpnpInputArgument(name = "NewTargetValue") String newTargetValue) {

            System.out.println("Changement");
            // [FACULTATIF] je garde la l'ancienne valeur pour emmettre l'evenenment
            String targetOldValue = target;
            target = newTargetValue;


            String statusOldValue = status;
            status = newTargetValue;

            // Envoie un �venement UPnP, c'est le nom de la variable d'etat qui lance l'�venement
            // COMMENCE PAR UNE MAJUSCULE. Ici "Status" pour la varialbe status
            getPropertyChangeSupport().firePropertyChange("Status", statusOldValue, status);

            // Fonctionne aussi :
            // getPropertyChangeSupport().firePropertyChange("Status", null null);

            // [FACULTATIF]
            // Ceci n'a pas d'effet pour le monitoring UPnP, mais fonctionne avec Javabean.
            // Ici on met le nom de la variable : status
            getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);

            status = State.AUCUN.toString();
            target = State.AUCUN.toString();
        }

        /**
         * Get target of the lamp
         * Methode Upnp grace au syst�me d'annotation
         *
         * @return boolean
         */
        @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
        public String getTarget() {
            return target;
        }

        /**
         * Get Status of the lamp
         * Methode Upnp grace au syst�me d'annotation
         *
         * @return boolean
         */
        @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
        public String getStatus() {
            // Pour ajouter des informations suppl�mentaires UPnP en cas d'erreur :
            // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
            return status;
        }

        /**
         * Print the version of the code
         * Ceci n'est pas une methode UPnP
         */
        public void printVersion() {
            System.out.println("Version : 1.0");
        }
}

